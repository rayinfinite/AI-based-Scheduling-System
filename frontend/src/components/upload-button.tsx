import React, { useRef } from "react";
import { Button } from "./ui/button";

interface ButtonProps {
  children?: React.ReactNode;
  onFileChange: (file: File | null) => void;
  accept?: string;
}

// 自定义的 UploadButton 组件
const UploadButton: React.FC<ButtonProps> = ({ onFileChange, children, accept = ".xlsx,.xls", ...props }) => {
  const hiddenFileInput = useRef<HTMLInputElement>(null);

  const handleButtonClick = () => {
    if (hiddenFileInput.current) {
      console.log("Triggering file input click"); // 调试信息
      hiddenFileInput.current.click();
    }
  };

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0] || null;
    console.log("Selected file:", file); // 调试信息
    onFileChange(file);
  };

  return (
    <>
      <Button onClick={handleButtonClick} {...props}>
        {children || <span>Click to Upload</span>}
      </Button>
      <input className="hidden" type="file" accept={accept} onChange={handleFileChange} ref={hiddenFileInput} />
    </>
  );
};

UploadButton.displayName = "UploadButton";

export { UploadButton };
