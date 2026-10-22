// import { Navigate, Outlet } from 'react-router-dom';
// import { useAuth } from '../context/AuthContext';

// export default function ProtectedRoute({ allowedRoles }) {
//   const { isAuthenticated, user } = useAuth();

//   if (!isAuthenticated) {
//     return <Navigate to="/login" replace />;
//   }

//   if (
//     allowedRoles?.length &&
//     !allowedRoles.includes(user?.role)
//   ) {
//     return <Navigate to="/forbidden" replace />;
//   }

//   return <Outlet />;
// }

import { Navigate, Outlet } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

export default function ProtectedRoute({ allowedRoles }) {
  const { isAuthenticated, user } = useAuth();

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  const userRole = String(user?.role || '').toUpperCase();

  if (
    allowedRoles?.length &&
    !allowedRoles.includes(userRole)
  ) {
    return <Navigate to="/forbidden" replace />;
  }

  return <Outlet />;
}