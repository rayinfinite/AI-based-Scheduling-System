import { memo, ReactNode, useEffect, useState } from "react";
import { getAllStudents, getAllTeachers, getCalenderData } from "./api";
import RayCalendar from "./components/calendar/RayCalendar";
import { EventInfo } from "./components/calendar/RayCalendarType";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "./components/ui/dialog";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuLabel,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import { Button } from "./components/ui/button";
import { format } from "date-fns";
import { EllipsisVertical } from "lucide-react";
import { MultiSelect } from "./components/multi-select";
import { Separator } from "./components/ui/separator";

interface EventDetailsProps {
  selectedEvent: EventInfo;
  handleCloseDetails: () => void;
}

const EventDetails: React.FC<EventDetailsProps> = memo(({ selectedEvent, handleCloseDetails }) => {
  const { title, start } = selectedEvent || {};
  const {
    practiceArea,
    courseCode,
    duration,
    software,
    cohort,
    run,
    classroom,
    teacher1,
    teacher2,
    teacher3,
    manager,
    cert,
  } = selectedEvent || {};

  const formatProperty = (label: string, value?: ReactNode) => (
    <p key={label} className="flex mb-2">
      <strong className="w-1/3 font-semibold">{label}:</strong> {value}
    </p>
  );

  return (
    <Dialog open={selectedEvent != null} onOpenChange={handleCloseDetails}>
      <DialogContent className="max-w-lg">
        <DialogHeader>
          <DialogTitle className="text-2xl font-bold">{title}</DialogTitle>
        </DialogHeader>
        <DialogDescription>
          {formatProperty("Practice Area", practiceArea)}
          {formatProperty("Course Code", courseCode)}
          {formatProperty("Duration (Days)", duration)}
          {formatProperty("Software", software)}
          {formatProperty("Cohort", cohort)}
          {formatProperty("Run #", run)}
          {formatProperty("Start", start ? new Date(start).toLocaleString() : "")}
          {formatProperty("Classroom", classroom)}
          {formatProperty("Teacher 1", teacher1)}
          {formatProperty("Teacher 2", teacher2)}
          {formatProperty("Teacher 3", teacher3)}
          {formatProperty("Manager", manager)}
          {formatProperty("Grad Cert (MTech)", cert)}
        </DialogDescription>
        <DialogFooter>
          <Button onClick={handleCloseDetails}>Close</Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
});

const MyRayCalendar: React.FC = () => {
  const [selectedEvent, setSelectedEvent] = useState<EventInfo | null>(null);

  const [selectedTeachers, setSelectedTeachers] = useState<string[]>([]);
  const [selectedStudents, setSelectedStudents] = useState<string[]>([]);

  const fetchEvents = async (start: Date, end: Date) => {
    const startString = format(start, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    const endString = format(end, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    const response = await getCalenderData(startString, endString, selectedTeachers, selectedStudents);
    return response;
  };

  const renderEventContent = (eventInfo: EventInfo) => {
    const title = eventInfo.title;
    const data = eventInfo;
    const color = getRandomColor(title);

    return (
      <div className={`p-1 px-2 cursor-pointer rounded my-1`} style={{ backgroundColor: color }}>
        <i className="text-[10px] sm:text-[14px] lg:text-[18px] font-bold text-gray-900 leading-none ">{title}</i>
        <div className="text-[8px] sm:text-[10px] lg:text-[14px] text-gray-700 flex justify-between mt-1 leading-none italic flex-col md:flex-row">
          <span>{data.classroom}</span>
          <span>{data.teacher1}</span>
        </div>
      </div>
    );
  };

  const handleEventClick = (eventInfo: EventInfo) => {
    setSelectedEvent(eventInfo);
  };

  const handleCloseDetails = () => {
    setSelectedEvent(null);
  };

  const resources = [
    "Room1",
    "Room2",
    "Room3",
    "Room4",
    "Room5",
    "Room6",
    "Room7",
    "Room8",
    "Room9",
    "Room10",
    "Room11",
    "Room12",
    "Room13",
    "Room14",
    "Room15",
    "Room16",
  ];

  const RightTitle = () => {
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

    return (
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
    );
  };

  return (
    <>
      <RayCalendar
        fetchEvents={fetchEvents}
        eventContent={renderEventContent}
        eventClick={handleEventClick}
        resources={resources}
        resourceHeader="Room"
        rightTitle={RightTitle()}
      />
      {selectedEvent && <EventDetails selectedEvent={selectedEvent} handleCloseDetails={handleCloseDetails} />}
    </>
  );
};

function getRandomColor(seed: string) {
  let hash = 0;
  for (let i = seed.length - 1; i >= 0; i--) {
    hash = seed.charCodeAt(i) + ((hash << 5) - hash);
  }
  let color = "#";
  for (let i = 0; i < 3; i++) {
    const value = (hash >> (i * 8)) & 0xff;
    color += ("00" + value.toString(16)).substr(-2);
  }
  return color;
}

export default MyRayCalendar;
