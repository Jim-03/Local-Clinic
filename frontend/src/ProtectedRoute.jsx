import { Navigate } from 'react-router-dom';

/**
 * A route protector that prevents mounting of component until a user logs in
 * @param {import('react-router-dom').RoutesProps} props - A route to protect
 * @returns {import('react').JSX.Element} A component that protects other routes until a user logs in
 */
function ProtectedRoute ({ children }) {
  // Check if user data is provided
  const user = JSON.parse(sessionStorage.getItem('user'));

  if (!user) {
    return <Navigate to='/login' replace />;
  }

  return children;
}

export default ProtectedRoute;
