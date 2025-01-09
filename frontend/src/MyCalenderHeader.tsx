import React, { useEffect, useState } from "react";
import { Button } from "@/components/ui/button";
import { ChevronLeft, ChevronRight, EllipsisVertical } from "lucide-react";
import { getAllStudents, getAllTeachers } from "./api";
import { DatePicker } from "./components/date-picker";
import { Tabs, TabsList, TabsTrigger } from "./components/ui/tabs";
import { MultiSelect } from "./components/multi-select";
import { CalendarApi } from "@fullcalendar/core/index.js";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuLabel,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import { Separator } from "./components/ui/separator";

const viewOptions = [
  { label: "Month", value: "dayGridMonth" },
  { label: "Week", value: "timeGridWeek" },
  { label: "Day", value: "timeGridDay" },
];

const Header: React.FC<{
  calendarApi: CalendarApi;
  selectedTeachers: string[];
  setSelectedTeachers: (teachers: string[]) => void;
  selectedStudents: string[];
  setSelectedStudents: (students: string[]) => void;
}> = ({ calendarApi, selectedTeachers, setSelectedTeachers, selectedStudents, setSelectedStudents }) => {
  const [radioValue, setRadioValue] = useState("dayGridMonth");
  const [weekend, setWeekend] = useState(true);
  const [teacher, setTeacher] = useState<{ label: string; value: string }[]>([]);
  const [students, setStudents] = useState<{ label: string; value: string }[]>([]);

  useEffect(() => {
    const fetchTeachers = async () => {
      const data: string[] = await getAllTeachers();
      const result = data.map((item: string) => ({ label: item, value: item }));
      setTeacher(result);

      const studentData = await getAllStudents();
      const studentResult = studentData.map((item: string) => ({ label: item, value: item }));
      setStudents(studentResult);
    };
    fetchTeachers();
  }, []);

  const changeTime = (date: Date | undefined) => {
    if (date) {
      calendarApi.gotoDate(date);
    }
  };

  const changeView = (value: string) => {
    calendarApi.changeView(value);
    setRadioValue(value);
  };

  const changeWeekendsVisible = () => {
    setWeekend(!weekend);
    calendarApi.setOption("weekends", !weekend);
  };

  useEffect(() => {
    if (calendarApi) {
      calendarApi.gotoDate(new Date(2024, 2, 1));
    }
  }, [calendarApi]);

  return (
    <div className="space-y-4">
      <div className={`flex flex-col lg:flex-row justify-between items-center gap-4 py-5`}>
        <div className="flex items-center gap-4">
          <Button variant="default" onClick={() => calendarApi?.today()}>
            Today
          </Button>
          <DatePicker onChange={changeTime} />
        </div>

        <div className="flex items-center gap-4">
          <Button className="rounded-full [&_svg]:size-6" size="icon" onClick={() => calendarApi.prev()}>
            <ChevronLeft />
          </Button>
          <h1 className="min-w-[240px] text-center">{calendarApi?.view.title}</h1>
          <Button className="rounded-full [&_svg]:size-6" size="icon" onClick={() => calendarApi.next()}>
            <ChevronRight />
          </Button>
        </div>

        <div className="flex items-center gap-4">
          <Button variant={weekend ? "default" : "outline"} onClick={changeWeekendsVisible}>
            Weekends
          </Button>
          <Tabs value={radioValue} onValueChange={changeView} className="flex gap-2">
            <TabsList>
              {viewOptions.map((option) => (
                <TabsTrigger key={option.value} value={option.value} onClick={() => changeView(option.value)}>
                  {option.label}
                </TabsTrigger>
              ))}
            </TabsList>
          </Tabs>

          <DropdownMenu>
            <DropdownMenuTrigger asChild>
              <Button variant="outline" size="icon">
                <EllipsisVertical className="w-4 h-4" />
              </Button>
            </DropdownMenuTrigger>
            <DropdownMenuContent className="w-[300px] mt-2 p-2" align="end">
              <DropdownMenuLabel>Select Lecturers</DropdownMenuLabel>
              <MultiSelect
                options={teacher}
                value={selectedTeachers}
                onValueChange={setSelectedTeachers}
                placeholder="Please select lecturers"
                className="w-full bg-white"
              />
              <Separator />
              <DropdownMenuLabel>Select Students</DropdownMenuLabel>
              <MultiSelect
                options={students}
                value={selectedStudents}
                onValueChange={setSelectedStudents}
                placeholder="Please select students"
                className="w-full bg-white"
              />
            </DropdownMenuContent>
          </DropdownMenu>
        </div>
      </div>
    </div>
  );
};

export default Header;
