import React, { useRef } from "react";
import { Button } from "@/components/ui/button";
import { getUser, ROOT_PATH, uploadExcel } from "./api";
import ClassroomTable from "./ClassroomTable";
import { Sheet, SheetTrigger, SheetContent, SheetHeader, SheetTitle, SheetDescription } from "@/components/ui/sheet";
import { Separator } from "./components/ui/separator";
import { Input } from "./components/ui/input";

const UploadExcel: React.FC = () => {
  const handleFileChange = async (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (!file) return;

    try {
      await uploadExcel(file);
      window.location.reload();
    } catch (error) {
      console.error("Upload failed:", error);
      alert("Upload failed");
    }
  };

  return <Input type="file" accept=".xlsx,.xls" onChange={handleFileChange} className="cursor-pointer" />;
};

const UploadPage: React.FC = () => {
  const classroomTableRef = useRef<{ add: () => void } | null>(null);

  const handleAddRow = () => {
    if (classroomTableRef.current) {
      classroomTableRef.current.add();
    }
  };

  const showDrawer = async () => {
    const data = await getUser();
    if (!data) {
      window.location.href = `${ROOT_PATH}/oauth2/authorization/azure`;
    }
  };

  return (
    <Sheet>
      <SheetTrigger asChild>
        <Button onClick={showDrawer}>
          Login & Input Course Data
        </Button>
      </SheetTrigger>
      <SheetContent className="w-[640px] max-w-full sm:max-w-[640px] gap-4 flex flex-col">
        <SheetHeader>
          <SheetTitle>Input Course Data</SheetTitle>
        </SheetHeader>
        <Separator />
        <div className="flex flex-row w-full gap-4">
          <UploadExcel />
          <Button onClick={handleAddRow}>Add new classroom</Button>
          <SheetDescription />
        </div>
        <ClassroomTable ref={classroomTableRef} />
      </SheetContent>
    </Sheet>
  );
};

export default UploadPage;
