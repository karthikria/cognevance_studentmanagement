
import { useEffect, useState } from 'react';
import api, { getErrorMessage } from '../api/axios';

export default function MyAttendance() {
  const [records, setRecords] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    let active = true;

    async function loadAttendance() {
      try {
        const response = await api.get('/attendance/my');

        if (active) {
          setRecords(
            Array.isArray(response.data) ? response.data : []
          );
        }
      } catch (err) {
        if (active) {
          setError(getErrorMessage(err));
        }
      } finally {
        if (active) setLoading(false);
      }
    }

    loadAttendance();

    return () => {
      active = false;
    };
  }, []);

  if (loading) return <p>Loading your attendance...</p>;
  if (error) return <p role="alert">{error}</p>;

  const presentCount = records.filter(
    (record) => record.status === 'PRESENT'
  ).length;

  const attendancePercentage =
    records.length > 0
      ? Math.round((presentCount / records.length) * 100)
      : 0;

  return (
    <section className="content-card">
      <h2>My Attendance</h2>

      <p>
        Attendance: {presentCount}/{records.length}
        {' '}({attendancePercentage}%)
      </p>

      {records.length === 0 ? (
        <p>No attendance records available yet.</p>
      ) : (
        <div className="table-container">
          <table>
            <thead>
              <tr>
                <th>Course</th>
                <th>Date</th>
                <th>Status</th>
                <th>Remarks</th>
              </tr>
            </thead>

            <tbody>
              {records.map((record) => (
                <tr key={record.id}>
                  <td>
                    {record.course?.title ||
                      record.course?.courseCode ||
                      '—'}
                  </td>
                  <td>{record.attendanceDate || '—'}</td>
                  <td>{record.status || '—'}</td>
                  <td>{record.remarks || '—'}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </section>
  );
}
