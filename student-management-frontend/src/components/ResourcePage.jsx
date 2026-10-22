import {
  useCallback,
  useEffect,
  useMemo,
  useState,
} from 'react';
import {
  Pencil,
  Plus,
  RefreshCw,
  Search,
  Trash2,
  X,
} from 'lucide-react';

import api, { getErrorMessage } from '../api/axios';

function displayValue(value) {
  if (value === null || value === undefined || value === '') return '—';
  if (typeof value === 'object') {
    return value.name || value.title || value.id || JSON.stringify(value);
  }
  return String(value);
}

export default function ResourcePage({
  title,
  description,
  endpoint,
  fields,
  columns,
  canCreate = true,
  canEdit = true,
  canDelete = true,
}) {
  const [records, setRecords] = useState([]);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState('');
  const [notice, setNotice] = useState('');
  const [search, setSearch] = useState('');
  const [modalOpen, setModalOpen] = useState(false);
  const [editing, setEditing] = useState(null);
  const [form, setForm] = useState({});
  const [formError, setFormError] = useState('');
const [dynamicOptions, setDynamicOptions] = useState({});
  const loadRecords = useCallback(async () => {
    setLoading(true);
    setError('');

    try {
      const { data } = await api.get(endpoint);
      const list = Array.isArray(data)
        ? data
        : data?.content || data?.data;

      if (!Array.isArray(list)) {
        throw new Error('Expected a list from the backend API.');
      }

      setRecords(list);
    } catch (err) {
      setError(getErrorMessage(err));
    } finally {
      setLoading(false);
    }
  }, [endpoint]);

  useEffect(() => {
    loadRecords();
  }, [loadRecords]);

useEffect(() => {
  let cancelled = false;

  async function loadDropdownOptions() {
    const fieldsWithSources = fields.filter(
      (field) => field.optionsFrom
    );

    const results = await Promise.all(
      fieldsWithSources.map(async (field) => {
        try {
          const { data } = await api.get(field.optionsFrom);

          const list = Array.isArray(data)
            ? data
            : data?.content || data?.data;

          if (!Array.isArray(list)) {
            return [field.name, []];
          }

          return [
            field.name,
            list.map((item) => ({
              value: String(item[field.optionValueKey || 'id']),
              label: [
                item[field.optionLabelKey || 'name'],
                field.optionSecondaryKey
                  ? item[field.optionSecondaryKey]
                  : null,
              ]
                .filter(Boolean)
                .join(' - '),
            })),
          ];
        } catch (error) {
          console.error(
            `Failed to load options for ${field.name}:`,
            error
          );
          return [field.name, []];
        }
      })
    );

    if (!cancelled) {
      setDynamicOptions(Object.fromEntries(results));
    }
  }

  loadDropdownOptions();

  return () => {
    cancelled = true;
  };
}, [fields]);

  const filteredRecords = useMemo(() => {
    const query = search.trim().toLowerCase();

    if (!query) return records;

    return records.filter((record) =>
      columns.some((column) =>
        displayValue(record[column.key]).toLowerCase().includes(query)
      )
    );
  }, [records, search, columns]);

  function openCreate() {
    setEditing(null);
    setForm(Object.fromEntries(fields.map((field) => [field.name, ''])));
    setFormError('');
    setModalOpen(true);
  }

  function openEdit(record) {
    setEditing(record);

    const values = Object.fromEntries(
      fields.map((field) => {
        const value = record[field.name];
        return [
          field.name,
          value == null
            ? ''
            : typeof value === 'object'
              ? value.id ?? ''
              : String(value),
        ];
      })
    );

    setForm(values);
    setFormError('');
    setModalOpen(true);
  }

  async function handleSubmit(event) {
    event.preventDefault();
    setFormError('');

    for (const field of fields) {
      if (field.required && !String(form[field.name] ?? '').trim()) {
        setFormError(`${field.label} is required.`);
        return;
      }
    }

    const payload = Object.fromEntries(
      fields.map((field) => [
        field.name,
        form[field.name] === ''
          ? null
          : ['number', 'select'].includes(field.type) &&
    field.name.endsWith('Id')
  ? Number(form[field.name])
  : field.type === 'number'
    ? Number(form[field.name])
    : form[field.name],
      ])
    );

    setSaving(true);

    try {
      if (editing) {
        if (editing.id == null) {
          throw new Error('Cannot update: record ID is missing.');
        }

        await api.put(`${endpoint}/${encodeURIComponent(editing.id)}`, payload);
      } else {
        await api.post(endpoint, payload);
      }

      setNotice(`${title} ${editing ? 'updated' : 'created'} successfully.`);
      setModalOpen(false);
      await loadRecords();
    } catch (err) {
      setFormError(getErrorMessage(err));
    } finally {
      setSaving(false);
    }
  }

  async function handleDelete(record) {
    if (record.id == null) {
      setError('Cannot delete: record ID is missing.');
      return;
    }

    if (!window.confirm(`Delete this ${title.toLowerCase()} record?`)) {
      return;
    }

    setError('');
    setNotice('');

    try {
      await api.delete(`${endpoint}/${encodeURIComponent(record.id)}`);
      setNotice(`${title} deleted successfully.`);
      await loadRecords();
    } catch (err) {
      setError(getErrorMessage(err));
    }
  }

  return (
    <div className="resource-page">
      <div className="page-heading">
        <div>
          <h2>{title}</h2>
          <p>{description}</p>
        </div>

        <div className="page-heading-actions">
          <button
            className="button button-secondary"
            type="button"
            onClick={loadRecords}
            disabled={loading}
          >
            <RefreshCw size={16} /> Refresh
          </button>

          {canCreate && (
            <button
              className="button button-primary"
              type="button"
              onClick={openCreate}
            >
              <Plus size={17} /> Add record
            </button>
          )}
        </div>
      </div>

      {error && (
        <div className="alert alert-error" role="alert">
          <span>{error}</span>
          <button className="alert-close" type="button" onClick={() => setError('')}>
            <X size={16} />
          </button>
        </div>
      )}

      {notice && (
        <div className="alert alert-success" role="status">
          <span>{notice}</span>
          <button className="alert-close" type="button" onClick={() => setNotice('')}>
            <X size={16} />
          </button>
        </div>
      )}

      <div className="resource-toolbar">
        <div className="search-field">
          <Search size={18} />
          <input
            type="search"
            placeholder={`Search ${title.toLowerCase()}...`}
            value={search}
            onChange={(event) => setSearch(event.target.value)}
          />
        </div>
        <span className="record-count">{filteredRecords.length} records</span>
      </div>

      <div className="table-card">
        {loading ? (
          <div className="loading-state">
            <span className="spinner" />
            <p>Loading {title.toLowerCase()}...</p>
          </div>
        ) : filteredRecords.length === 0 ? (
          <div className="empty-state">
            <Search size={26} />
            <h3>{search ? 'No matching records' : 'No records yet'}</h3>
            <p>Records returned by your backend will appear here.</p>
          </div>
        ) : (
          <div className="table-scroll">
            <table className="data-table">
              <thead>
                <tr>
                  {columns.map((column) => (
                    <th key={column.key}>{column.label}</th>
                  ))}
                  {(canEdit || canDelete) && <th>Actions</th>}
                </tr>
              </thead>

              <tbody>
                {filteredRecords.map((record, index) => (
                  <tr key={record.id ?? index}>
                    {columns.map((column) => (
                      <td key={column.key}>
                       {displayValue(
  column.key === 'studentId'
    ? record.student?.id
    : column.key === 'courseId'
      ? record.course?.id
      : record[column.key]
)}
                      </td>
                    ))}

                    {(canEdit || canDelete) && (
                      <td>
                        <div className="table-actions">
                          {canEdit && (
                            <button
                              className="icon-button action-edit"
                              type="button"
                              aria-label="Edit record"
                              onClick={() => openEdit(record)}
                            >
                              <Pencil size={16} />
                            </button>
                          )}
                          {canDelete && (
                            <button
                              className="icon-button action-delete"
                              type="button"
                              aria-label="Delete record"
                              onClick={() => handleDelete(record)}
                            >
                              <Trash2 size={16} />
                            </button>
                          )}
                        </div>
                      </td>
                    )}
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>

      {modalOpen && (
        <div className="modal-backdrop">
          <section className="modal-card" role="dialog" aria-modal="true">
            <div className="modal-header">
              <div>
                <h2>{editing ? 'Edit' : 'Add'} {title}</h2>
                <p>Enter the record details below.</p>
              </div>
              <button
                className="icon-button"
                type="button"
                aria-label="Close form"
                onClick={() => setModalOpen(false)}
                disabled={saving}
              >
                <X size={20} />
              </button>
            </div>

            <form onSubmit={handleSubmit}>
              <div className="form-grid">
                {fields.map((field) => (
                  <div className="form-field" key={field.name}>
                    <label htmlFor={`field-${field.name}`}>
                      {field.label}{field.required ? ' *' : ''}
                    </label>

                    {(field.options || dynamicOptions[field.name]) ? (
                      <select
                        id={`field-${field.name}`}
                        value={form[field.name] ?? ''}
                        onChange={(event) =>
                          setForm((old) => ({
                            ...old,
                            [field.name]: event.target.value,
                          }))
                        }
                        required={field.required}
                      >
                        <option value="">Select {field.label}</option>
                        {(field.options || dynamicOptions[field.name] || []).map((option) => (
                          <option key={option.value} value={option.value}>
                            {option.label}
                          </option>
                        ))}
                      </select>
                    ) : (
                      <input
                        id={`field-${field.name}`}
                        type={field.type || 'text'}
                        value={form[field.name] ?? ''}
                        onChange={(event) =>
                          setForm((old) => ({
                            ...old,
                            [field.name]: event.target.value,
                          }))
                        }
                        required={field.required}
                        min={field.min}
                        max={field.max}
                      />
                    )}
                  </div>
                ))}
              </div>

              {formError && (
                <div className="alert alert-error" role="alert">
                  {formError}
                </div>
              )}

              <div className="modal-footer">
                <button
                  className="button button-secondary"
                  type="button"
                  onClick={() => setModalOpen(false)}
                  disabled={saving}
                >
                  Cancel
                </button>
                <button
                  className="button button-primary"
                  type="submit"
                  disabled={saving}
                >
                  {saving ? 'Saving...' : editing ? 'Save changes' : 'Create record'}
                </button>
              </div>
            </form>
          </section>
        </div>
      )}
    </div>
  );
}