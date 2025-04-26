import toast from "react-hot-toast";

/**
 * Checks if the patient's data is provided
 * @param {Object} user The patient's data
 * @returns A patient object
 */
export function validData(user) {
  const requiredFields = Object.keys(user);

  for (const field of requiredFields) {
    if (!user[field]) {
      toast.error("Please fill in all fields!");
      return null;
    }
  }

  return user;
}
