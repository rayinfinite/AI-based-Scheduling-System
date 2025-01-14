import React, { memo, useCallback, useEffect, useRef, useState } from "react";
import FullCalendar from "@fullcalendar/react";
import { CalendarApi, EventApi, EventContentArg, EventSourceFunc, formatDate } from "@fullcalendar/core";
import dayGridPlugin from "@fullcalendar/daygrid";
import timeGridPlugin from "@fullcalendar/timegrid";
import interactionPlugin from "@fullcalendar/interaction";
import { getCalenderData } from "./api";
import Header from "./MyCalenderHeader";
import { Button } from "@/components/ui/button";
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogDescription,
  DialogFooter,
} from "@/components/ui/dialog";

interface EventDetailsProps {
  selectedEvent: EventApi | null;
  handleCloseDetails: () => void;
}

const EventDetails: React.FC<EventDetailsProps> = memo(({ selectedEvent, handleCloseDetails }) => {
  const { title, extendedProps, start } = selectedEvent || {};
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
  } = extendedProps || {};

  const formatProperty = (label: string, value?: string) => (
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

const MyCalendar: React.FC = () => {
  const calendarRef = useRef<FullCalendar | null>(null);
  const [calendarApi, setCalendarApi] = useState<CalendarApi>();
  const [events, setEvents] = useState<EventApi[]>([]);
  const [selectedEvent, setSelectedEvent] = useState<EventApi | null>(null);
  const [selectedTeachers, setSelectedTeachers] = useState<string[]>([]);
  const [selectedStudents, setSelectedStudents] = useState<string[]>([]);

  useEffect(() => {
    const calendarApi = calendarRef.current?.getApi();
    if (calendarApi) {
      setCalendarApi(calendarApi);
      setEvents(calendarApi.getEvents());
    }
  }, []);

  const handleEventClick = (clickInfo: { event: EventApi }) => {
    setSelectedEvent(clickInfo.event);
  };

  const handleCloseDetails = () => {
    setSelectedEvent(null);
  };

  const renderEventContent = (eventInfo: EventContentArg) => {
    const title = eventInfo.event.title;
    const data = eventInfo.event.extendedProps;
    const color = getRandomColor(title);

    return (
      <div className={`p-1 px-2 cursor-pointer`} style={{ backgroundColor: color }}>
        <i className="text-[10px] sm:text-[14px] lg:text-[18px] font-bold text-gray-900 leading-none ">{title}</i>
        <div className="text-[8px] sm:text-[10px] lg:text-[14px] text-gray-700 flex justify-between mt-1 leading-none italic flex-col md:flex-row">
          <span>{data.classroom}</span>
          <span>{data.teacher1}</span>
        </div>
      </div>
    );
  };

  const fetchEvents: EventSourceFunc = useCallback(
    async (fetchInfo, successCallback, failureCallback) => {
      try {
        const { start, end } = fetchInfo;
        const data = await getCalenderData(start.toISOString(), end.toISOString(), selectedTeachers, selectedStudents);
        setEvents(data);
        successCallback(data);
      } catch (err) {
        failureCallback(err as Error);
      }
    },
    [selectedTeachers, selectedStudents]
  );

  return (
    <div>
      {calendarApi && (
        <Header
          calendarApi={calendarApi}
          selectedTeachers={selectedTeachers}
          setSelectedTeachers={setSelectedTeachers}
          selectedStudents={selectedStudents}
          setSelectedStudents={setSelectedStudents}
        />
      )}
      <div>
        <FullCalendar
          ref={calendarRef}
          plugins={[dayGridPlugin, timeGridPlugin, interactionPlugin]}
          headerToolbar={false}
          initialView="dayGridMonth"
          selectMirror={true}
          dayMaxEvents={true}
          events={fetchEvents}
          eventContent={renderEventContent} // custom render function
          eventClick={handleEventClick}
          slotMinTime={"08:00:00"}
          slotMaxTime={"20:00:00"}
          slotDuration={"01:00:00"}
          contentHeight={1500}
        />
      </div>
      <EventDetails selectedEvent={selectedEvent} handleCloseDetails={handleCloseDetails} />
      <Sidebar events={events} />
    </div>
  );
};

function getRandomColor(seed: string) {
  let hash = 0;
  for (let i = 0; i < seed.length; i++) {
    hash = seed.charCodeAt(i) + ((hash << 5) - hash);
  }
  let color = "#";
  for (let i = 0; i < 3; i++) {
    const value = (hash >> (i * 8)) & 0xff;
    color += ("00" + value.toString(16)).substr(-2);
  }
  return color;
}

export default MyCalendar;

const Sidebar: React.FC<{ events: EventApi[] }> = ({ events }) => {
  function renderSidebarEvent(event: EventApi) {
    return (
      <li key={event.id}>
        <b>
          {event.start &&
            formatDate(event.start, {
              year: "numeric",
              month: "short",
              day: "numeric",
            })}
        </b>
        <i>{event.title}</i>
      </li>
    );
  }
  return (
    <div>
      <h2>All Events ({events.length})</h2>
      <ul>{events.map(renderSidebarEvent)}</ul>
    </div>
  );
};
