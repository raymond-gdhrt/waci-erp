import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { Button } from "@/components/ui/button";
import { PencilIcon, TrashIcon } from "lucide-react";
import type { Member } from "@/types/member";

interface MemberTableProps {
  members: Member[];
  currentPage: number;
  totalMembers: number;
  pageSize: number;
  onPageChange: (page: number) => void;
  onEdit: (member: Member) => void;
  onDelete: (id: string) => void;
}

export function MemberTable({
  members,
  currentPage,
  totalMembers,
  pageSize,
  onPageChange,
  onEdit,
  onDelete,
}: MemberTableProps) {
  const totalPages = Math.ceil(totalMembers / pageSize);

  return (
    <div>
      <Table>
        <TableHeader>
          <TableRow>
            <TableHead>First Name</TableHead>
            <TableHead>Last Name</TableHead>
            <TableHead>Email</TableHead>
            <TableHead>Phone Number</TableHead>
            <TableHead>Actions</TableHead>
          </TableRow>
        </TableHeader>
        <TableBody>
          {members.map((member) => (
            <TableRow key={member.id}>
              <TableCell>{member.firstName}</TableCell>
              <TableCell>{member.lastName}</TableCell>
              <TableCell>{member.email}</TableCell>
              <TableCell>{member.phoneNumber}</TableCell>
              <TableCell>
                <Button variant="ghost" onClick={() => onEdit(member)}>
                  <PencilIcon className="h-4 w-4" />
                </Button>
                <Button variant="ghost" onClick={() => onDelete(member.id)}>
                  <TrashIcon className="h-4 w-4" />
                </Button>
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
      <div className="flex justify-between items-center mt-4">
        <div>
          Showing {(currentPage - 1) * pageSize + 1} to{" "}
          {Math.min(currentPage * pageSize, totalMembers)} of {totalMembers} members
        </div>
        <div>
          <Button onClick={() => onPageChange(currentPage - 1)} disabled={currentPage === 1}>
            Previous
          </Button>
          <Button
            onClick={() => onPageChange(currentPage + 1)}
            disabled={currentPage === totalPages}
          >
            Next
          </Button>
        </div>
      </div>
    </div>
  );
}
