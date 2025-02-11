import React, { useEffect, useRef, useState } from "react";
import { Button } from "@/components/ui/button";
import { downloadExcel, uploadExcel, uploadResultExcel } from "./api";
import ClassroomTable from "./ClassroomTable";
import { Sheet, SheetTrigger, SheetContent, SheetHeader, SheetTitle, SheetDescription } from "@/components/ui/sheet";
import { Separator } from "./components/ui/separator";
import { Download, Upload } from "lucide-react";
import { UploadButton } from "./components/upload-button";

const UploadExcel: React.FC<{ disabled: boolean }> = ({ disabled }) => {
  const handleFileChange = async (file: File | null) => {
    if (!file) return;
    try {
      await uploadExcel(file);
    } catch (error) {
      console.error("Upload failed:", error);
      alert("Upload failed");
    }
  };

  return (
    <div>
      <label className="block mb-2 text-sm font-medium text-gray-900 dark:text-white" htmlFor="file_input">
        Upload Excel
      </label>
      <UploadButton onFileChange={handleFileChange} disabled={disabled}>
        <Upload className="w-4 h-4 mr-2" /> Scheduling Upload
      </UploadButton>
    </div>
  );
};

const UploadResultExcel: React.FC<{ disabled: boolean }> = ({ disabled }) => {
  const handleFileChange = async (file: File | null) => {
    if (!file) return;
    try {
      await uploadResultExcel(file);
    } catch (error) {
      console.error("Upload failed:", error);
      alert("Upload failed");
    }
  };

  return (
    <div>
      <label className="block mb-2 text-sm font-medium text-gray-900 dark:text-white" htmlFor="file_input">
        Result Upload
      </label>
      <UploadButton onFileChange={handleFileChange} disabled={disabled}>
        <Upload className="w-4 h-4 mr-2" /> Result Upload
      </UploadButton>
    </div>
  );
};

const UploadPage: React.FC = () => {
  const classroomTableRef = useRef<{ add: () => void } | null>(null);
  const [isRunning, setIsRunning] = useState(false);

  useEffect(() => {
    const eventSource = new EventSource("/upload/status");

    eventSource.onmessage = (event) => {
      const data = JSON.parse(event.data);
      setIsRunning(data.running);
    };

    return () => eventSource.close(); // Close connection when component is uninstalled
  }, []);

  const handleAddRow = () => {
    if (classroomTableRef.current) {
      classroomTableRef.current.add();
    }
  };

  const downloadFile = async () => {
    try {
      await downloadExcel();
    } catch (error) {
      console.error("Download failed:", error);
      alert("Download failed");
    }
  };

  return (
    <div className="flex flex-col flex-wrap justify-center gap-4 md:flex-row md:justify-start">
      <div className="flex items-end justify-center gap-4">
        <UploadExcel disabled={isRunning} />
        <Button onClick={downloadFile} className="flex items-center gap-2">
          <Download />
          Export Excel
        </Button>
      </div>
      <div className="flex items-end justify-center gap-4">
        <UploadResultExcel disabled={isRunning} />
        <Sheet>
          <SheetTrigger asChild>
            <Button>Classroom Config</Button>
          </SheetTrigger>
          <SheetContent className="w-[640px] max-w-full sm:max-w-[640px] gap-4 flex flex-col overflow-y-auto">
            <SheetHeader>
              <SheetTitle>Classroom Information</SheetTitle>
            </SheetHeader>
            <Separator />
            <div className="flex flex-row w-full gap-4">
              <Button onClick={handleAddRow}>Add new classroom</Button>
              <SheetDescription />
            </div>
            <ClassroomTable ref={classroomTableRef} />
          </SheetContent>
        </Sheet>
      </div>
    </div>
  );
};

export default UploadPage;
