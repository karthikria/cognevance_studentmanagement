import { Navigate, Route, Routes } from 'react-router-dom';

import { useAuth } from './context/AuthContext';
import ProtectedRoute from './components/ProtectedRoute';
import Layout from './components/Layout';
import ResourcePage from './components/ResourcePage';
import MyMarks from './pages/MyMarks';
import Login from './pages/Login';
import Dashboard from './pages/Dashboard';
import Forbidden from './pages/Forbidden';
import MyAttendance from './pages/MyAttendance';
const ADMIN = ['ADMIN'];
const ALL_ROLES = ['ADMIN', 'TEACHER', 'STUDENT'];

const studentFields = [
  { name: 'studentNumber', label: 'Student number', required: true },
  { name: 'name', label: 'Full name', required: true },
  { name: 'email', label: 'Email', type: 'email', required: true },
  {
  name: 'initialPassword',
  label: 'Initial password',
  type: 'password',
  required: true,
  minLength: 8,
},
  { name: 'phone', label: 'Phone', type: 'tel' },
  { name: 'department', label: 'Department', required: true },
  {
    name: 'yearOfStudy',
    label: 'Year of study',
    type: 'number',
    required: true,
    min: 1,
    max: 8,
  },
];

const teacherFields = [
  { name: 'employeeNumber', label: 'Employee number', required: true },
  { name: 'name', label: 'Full name', required: true },
  { name: 'email', label: 'Email', type: 'email', required: true },
  {
  name: 'initialPassword',
  label: 'Initial password',
  type: 'password',
  required: true,
  minLength: 8,
},
  { name: 'department', label: 'Department', required: true },
  { name: 'phone', label: 'Phone', type: 'tel' },
];

const courseFields = [
  { name: 'courseCode', label: 'Course code', required: true },
  { name: 'title', label: 'Course title', required: true },
  { name: 'description', label: 'Description' },
  {
    name: 'credits',
    label: 'Credits',
    type: 'number',
    required: true,
    min: 1,
    max: 10,
  },
  {
  name: 'teacherId',
  label: 'Teacher',
  type: 'select',
  optionsFrom: '/teachers',
  optionValueKey: 'id',
  optionLabelKey: 'name',
  optionSecondaryKey: 'employeeNumber',
},
];


const enrollmentFields = [
  {
    name: 'studentId',
    label: 'Student',
    type: 'select',
    required: true,
    optionsFrom: '/students',
    optionValueKey: 'id',
    optionLabelKey: 'name',
    optionSecondaryKey: 'studentNumber',
  },
  {
    name: 'courseId',
    label: 'Course',
    type: 'select',
    required: true,
    optionsFrom: '/courses',
    optionValueKey: 'id',
    optionLabelKey: 'title',
    optionSecondaryKey: 'courseCode',
  },
];


const attendanceFields = [
  {
    name: 'studentId',
    label: 'Student',
    type: 'select',
    required: true,
    optionsFrom: '/students',
    optionValueKey: 'id',
    optionLabelKey: 'name',
    optionSecondaryKey: 'studentNumber',
  },
  {
    name: 'courseId',
    label: 'Course',
    type: 'select',
    required: true,
    optionsFrom: '/courses',
    optionValueKey: 'id',
    optionLabelKey: 'title',
    optionSecondaryKey: 'courseCode',
  },
  {
    name: 'attendanceDate',
    label: 'Attendance date',
    type: 'date',
    required: true,
  },
  {
    name: 'status',
    label: 'Status',
    required: true,
    options: [
      { value: 'PRESENT', label: 'Present' },
      { value: 'ABSENT', label: 'Absent' },
      { value: 'LATE', label: 'Late' },
      { value: 'EXCUSED', label: 'Excused' },
    ],
  },
  { name: 'remarks', label: 'Remarks' },
];



const markFields = [
  {
    name: 'studentId',
    label: 'Student',
    type: 'select',
    required: true,
    optionsFrom: '/students',
    optionValueKey: 'id',
    optionLabelKey: 'name',
    optionSecondaryKey: 'studentNumber',
  },
  {
    name: 'courseId',
    label: 'Course',
    type: 'select',
    required: true,
    optionsFrom: '/courses',
    optionValueKey: 'id',
    optionLabelKey: 'title',
    optionSecondaryKey: 'courseCode',
  },
  {
    name: 'assessmentName',
    label: 'Assessment name',
    required: true,
  },
  {
    name: 'score',
    label: 'Score',
    type: 'number',
    required: true,
    min: 0,
  },
  {
    name: 'maxScore',
    label: 'Maximum score',
    type: 'number',
    required: true,
    min: 0.1,
  },
  {
    name: 'assessmentDate',
    label: 'Assessment date',
    type: 'date',
    required: true,
  },
];


function HomeRedirect() {
  const { isAuthenticated } = useAuth();

  return (
    <Navigate
      to={isAuthenticated ? '/dashboard' : '/login'}
      replace
    />
  );
}

export default function App() {
  const { user } = useAuth();
  const role = String(user?.role || '').toUpperCase();

  const isAdmin = role === 'ADMIN';
  const canManageAcademicRecords =
    role === 'ADMIN' || role === 'TEACHER';
 
  return (
    
    <Routes>
      <Route path="/" element={<HomeRedirect />} />
      <Route path="/login" element={<Login />} />
      <Route path="/forbidden" element={<Forbidden />} />

      <Route element={<ProtectedRoute allowedRoles={ALL_ROLES} />}>
        <Route element={<Layout />}>
          <Route path="/dashboard" element={<Dashboard />} />

          <Route
            path="/courses"
            element={
              <ResourcePage
                title="Courses"
                description="Browse academic courses."
                endpoint="/courses"
                fields={courseFields}
                columns={[
                  { key: 'courseCode', label: 'Course code' },
                  { key: 'title', label: 'Title' },
                  { key: 'credits', label: 'Credits' },
                  { key: 'teacherId', label: 'Teacher ID' },
                ]}
                canCreate={isAdmin}
                canEdit={isAdmin}
                canDelete={isAdmin}
              />
            }
          />

          <Route
            path="/attendance"
            element={
              <ResourcePage
                title="Attendance"
                description="Review attendance records."
                endpoint="/attendance"
                fields={attendanceFields}
                columns={[
                  { key: 'student', label: 'Student' },
                  { key: 'course', label: 'Course' },
                  { key: 'attendanceDate', label: 'Date' },
                  { key: 'status', label: 'Status' },
                  { key: 'remarks', label: 'Remarks' },
                ]}
                canCreate={canManageAcademicRecords}
                canEdit={canManageAcademicRecords}
                canDelete={isAdmin}
              />
            }
          />

          <Route
            path="/marks"
            element={
              <ResourcePage
                title="Marks"
                description="Review assessment results."
                endpoint="/marks"
                fields={markFields}
                
columns={[
  { key: 'student', label: 'Student' },
  { key: 'course', label: 'Course' },
  { key: 'assessmentName', label: 'Assessment' },
  { key: 'score', label: 'Score' },
  { key: 'maxScore', label: 'Maximum score' },
  { key: 'assessmentDate', label: 'Date' },
]}

                canCreate={canManageAcademicRecords}
                canEdit={canManageAcademicRecords}
                canDelete={isAdmin}
              />
            }
          />
        </Route>
      </Route>

      <Route element={<ProtectedRoute allowedRoles={ADMIN} />}>
        <Route element={<Layout />}>
          <Route
            path="/students"
            element={
              <ResourcePage
                title="Students"
                description="Manage student profiles and academic details."
                endpoint="/students"
                fields={studentFields}
                columns={[
                  { key: 'studentNumber', label: 'Student No.' },
                  { key: 'name', label: 'Name' },
                  { key: 'email', label: 'Email' },
                  { key: 'department', label: 'Department' },
                  { key: 'yearOfStudy', label: 'Year' },
                ]}
              />
            }
          />

          <Route
            path="/teachers"
            element={
              <ResourcePage
                title="Teachers"
                description="Manage teacher profiles and departments."
                endpoint="/teachers"
                fields={teacherFields}
                columns={[
                  { key: 'employeeNumber', label: 'Employee No.' },
                  { key: 'name', label: 'Name' },
                  { key: 'email', label: 'Email' },
                  { key: 'department', label: 'Department' },
                  { key: 'phone', label: 'Phone' },
                ]}
              />
            }
          />

          <Route
            path="/enrollments"
            element={
              <ResourcePage
                title="Enrollments"
                description="Manage student course enrollment records."
                endpoint="/enrollments"
                fields={enrollmentFields}
                columns={[
                  { key: 'id', label: 'Enrollment ID' },
                  { key: 'studentId', label: 'Student ID' },
                  { key: 'courseId', label: 'Course ID' },
                ]}
                canEdit={false}
                canDelete={false}
              />
            }
          />
        </Route>
      
      </Route>

<Route element={<ProtectedRoute allowedRoles={['STUDENT']} />}>
  <Route element={<Layout />}>
    <Route path="/my-marks" element={<MyMarks />} />
    <Route path="/my-attendance" element={<MyAttendance />} />
  </Route>
</Route>

      <Route path="*" element={<HomeRedirect />} />
    </Routes>
  );
}