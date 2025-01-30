import type { Metadata } from "next";
import "./globals.css";
import { setupSuperUser } from "@/lib/setupSuperUser";
export const metadata: Metadata = {
  title: "v0 App",
  description: "Created with v0",
};
setupSuperUser();
export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en">
      <body>{children}</body>
    </html>
  );
}
