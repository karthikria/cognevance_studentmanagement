
import { useEffect, useState } from 'react';
import api, { getErrorMessage } from '../api/axios';

export default function MyMarks() {
  const [marks, setMarks] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    let active = true;

    async function loadMarks() {
      try {
        const response = await api.get('/marks/my');

        if (active) {
          setMarks(Array.isArray(response.data) ? response.data : []);
        }
      } catch (err) {
        if (active) setError(getErrorMessage(err));
      } finally {
        if (active) setLoading(false);
      }
    }

    loadMarks();

    return () => {
      active = false;
    };
  }, []);

  if (loading) return <p>Loading your marks...</p>;
  if (error) return <p role="alert">{error}</p>;

  return (
    <section className="content-card">
      <h2>My Marks</h2>

      {marks.length === 0 ? (
        <p>No marks available yet.</p>
      ) : (
        <div className="table-container">
          <table>
            <thead>
              <tr>
                <th>Course</th>
                <th>Assessment</th>
                <th>Score</th>
                <th>Maximum</th>
                <th>Date</th>
              </tr>
            </thead>
            <tbody>
              {marks.map((mark) => (
                <tr key={mark.id}>
                  <td>
                    {mark.course?.title || mark.course?.courseCode || '—'}
                  </td>
                  <td>{mark.assessmentName}</td>
                  <td>{mark.score}</td>
                  <td>{mark.maxScore}</td>
                  <td>{mark.assessmentDate || '—'}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </section>
  );
}
