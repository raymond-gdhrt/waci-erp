import { supabase } from "./supabase";

export async function setupSuperUser() {
  // Check if a super user already exists
  const { data: existingSuperUser, error: fetchError } = await supabase
    .from("users")
    .select("id")
    .eq("role", "superuser")
    .single();

  if (fetchError && fetchError.code !== "PGRST116") {
    console.error("Error checking for existing super user:", fetchError);
    return;
  }

  if (existingSuperUser) {
    console.log("Super user already exists");
    return;
  }

  // If no super user exists, create one
  const superUserEmail = "admin@waci.com";
  const superUserPassword = "password123"; // You should change this to a secure password

  const { data: authData, error: signUpError } = await supabase.auth.signUp({
    email: superUserEmail,
    password: superUserPassword,
  });

  if (signUpError) {
    console.error("Error creating super user auth:", signUpError);
    return;
  }

  if (authData.user) {
    // Insert the user into the users table with the superuser role
    const { error: insertError } = await supabase.from("users").insert({
      id: authData.user.id,
      email: superUserEmail,
      role: "superuser",
    });

    if (insertError) {
      console.error("Error inserting super user into users table:", insertError);
    } else {
      console.log("Super user created successfully");
    }
  }
}
