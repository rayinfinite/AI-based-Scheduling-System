import {
  startOfMonth,
  getDaysInMonth,
  subMonths,
  format,
  subDays,
  addMonths,
  addDays,
  startOfWeek,
  Day,
  isSameMonth,
} from "date-fns";
import { useImperativeHandle, forwardRef, useState, useMemo, useEffect, useRef, useCallback } from "react";
import RayCalendarTable from "./RayCalendarTable";
import RayCalendarToolbar from "./RayCalendarToolbar";
import { CalendarApi, EventInfo, RayCalendarProps } from "./RayCalendarType";
import RayCalendarResourceTable from "./RayCalendarResourceTable";

const WEEKDAYS = ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"];

const Calendar = forwardRef<CalendarApi, RayCalendarProps>((props, ref) => {
  const {
    weekStartsOn = 1,
    showOutsideDays = true,
    fixedWeeks = true,
    fetchEvents,
    eventContent,
    eventClick,
    resourceHeader,
    resources,
    leftTitle,
    rightTitle,
  } = props;
  const [date, setDate] = useState(new Date());
  const [events, setEvents] = useState<EventInfo[]>([]);
  const [view, setView] = useState("Month");
  const [refresh, setRefresh] = useState(false);

  const { adjustedWeekdays, grid, start, end, title } = useMemo(() => {
    const adjustedWeekdays = WEEKDAYS.slice(weekStartsOn).concat(WEEKDAYS.slice(0, weekStartsOn));
    if (view === "Month") {
      const firstDay = startOfMonth(date);
      const firstDayOfMonth = startOfMonth(date).getDay();
      const daysInMonth = getDaysInMonth(date);
      const offset = (firstDayOfMonth - weekStartsOn + 7) % 7;
      const rows = fixedWeeks ? 6 : Math.ceil((offset + daysInMonth) / 7);
      const grid = Array.from({ length: rows }, () => Array(7).fill(null));

      const daysInPrevMonth = getDaysInMonth(subMonths(date, 1));

      let day = 1;
      for (let i = 0; i < rows; i++) {
        for (let j = 0; j < 7; j++) {
          if (i === 0 && j < offset && showOutsideDays) {
            const prevMonthDate = subDays(firstDay, offset - j);
            grid[i][j] = {
              day: daysInPrevMonth - offset + j + 1,
              outside: true,
              date: format(prevMonthDate, "yyyy-MM-dd"),
            };
          } else if (day > daysInMonth && showOutsideDays) {
            const nextMonthDate = addDays(firstDay, day - 1);
            grid[i][j] = { day: day++ - daysInMonth, outside: true, date: format(nextMonthDate, "yyyy-MM-dd") };
          } else {
            const thisMonthDate = addDays(firstDay, day - 1);
            grid[i][j] = { day: day++, outside: false, date: format(thisMonthDate, "yyyy-MM-dd") };
          }
        }
      }

      const start = showOutsideDays ? subDays(firstDay, offset) : firstDay;
      const nextMonth = startOfMonth(addMonths(date, 1));
      const end = showOutsideDays ? addDays(nextMonth, day - daysInMonth - 1) : nextMonth;
      const title = format(date, "MMMM yyyy");
      return { adjustedWeekdays, grid, start, end, title };
    } else {
      const firstDay = startOfWeek(date, { weekStartsOn: weekStartsOn as Day });
      const grid = Array.from({ length: 1 }, () =>
        Array.from({ length: 7 }, (_, i) => {
          const dayDate = addDays(firstDay, i);
          return {
            day: i,
            date: format(dayDate, "yyyy-MM-dd"),
          };
        })
      );
      const start = firstDay;
      const end = addDays(firstDay, 7);
      const weekdays = adjustedWeekdays.map((day, index) => `${day} ${format(addDays(firstDay, index), "M/d")}`);
      const title = isSameMonth(firstDay, end)
        ? `${format(firstDay, "MMM d")} – ${format(end, "d, yyyy")}`
        : `${format(firstDay, "MMM d")} – ${format(end, "MMM d, yyyy")}`;
      return { adjustedWeekdays: weekdays, grid, start, end, title };
    }
  }, [date, weekStartsOn, showOutsideDays, fixedWeeks, view]);

  const calendarApi: CalendarApi = useMemo(
    () => ({
      title: title,
      date: date,
      view: view,
      getDate: () => date,
      gotoDate: (date: Date) => setDate(date),
      today: () => setDate(new Date()),
      prev: () => (view === "Month" ? setDate(subMonths(date, 1)) : setDate(subDays(date, 7))),
      next: () => (view === "Month" ? setDate(addMonths(date, 1)) : setDate(addDays(date, 7))),
      prevYear: () => setDate(subMonths(date, 12)),
      nextYear: () => setDate(addMonths(date, 12)),
      incrementDays: (duration: number) => setDate(addDays(date, duration)),
      changeView: (value: string) => setView(value),
      refreshEvents: () => setRefresh(true),
    }),
    [date, title, view]
  );

  useImperativeHandle(ref, () => calendarApi, [calendarApi]);

  const prevStartRef = useRef<Date | null>(null);
  const prevEndRef = useRef<Date | null>(null);

  const fetchFunc = useCallback(() => {
    const timeZone = Intl.DateTimeFormat().resolvedOptions().timeZone;
    fetchEvents(start, end, timeZone).then((events) => {
      setEvents(events);
      prevStartRef.current = start;
      prevEndRef.current = end;
    });
  }, [start, end, fetchEvents]);

  useEffect(() => {
    if (prevStartRef.current !== start || prevEndRef.current !== end) {
      fetchFunc();
    }
  }, [fetchEvents, start, end, fetchFunc]);

  useEffect(() => {
    if (refresh) {
      fetchFunc();
      setRefresh(false);
    }
  }, [refresh, fetchFunc]);

  return (
    <div>
      <RayCalendarToolbar calendarApi={calendarApi} leftTitle={leftTitle} rightTitle={rightTitle} />
      {view == "Month" && (
        <RayCalendarTable
          weekTitle={adjustedWeekdays}
          monthGrid={grid}
          cellHeight={200}
          events={events}
          eventContent={eventContent}
          eventClick={eventClick}
        />
      )}
      {view == "Resource" && resources && (
        <RayCalendarResourceTable
          resourceHeader={resourceHeader || "Resource"}
          resources={resources}
          weekTitle={adjustedWeekdays}
          weekGrid={grid}
          cellHeight={60}
          events={events}
          eventContent={eventContent}
          eventClick={eventClick}
        />
      )}
    </div>
  );
});

export default Calendar;
