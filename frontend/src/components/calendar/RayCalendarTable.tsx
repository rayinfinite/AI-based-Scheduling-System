import { format } from "date-fns";
import { EventInfo } from "./RayCalendarType";
import { memo } from "react";

export interface RayCalendarTableProps {
  weekTitle: string[];
  monthGrid: { day: number; outside: boolean; date: string }[][];
  cellHeight: number;
  events: EventInfo[];
  eventContent?: (event: EventInfo) => JSX.Element;
  eventClick?: (event: EventInfo) => void;
}

const RayCalendarTable: React.FC<RayCalendarTableProps> = ({
  weekTitle,
  monthGrid,
  cellHeight,
  events,
  eventContent,
  eventClick,
}) => {
  const eventsMap = new Map<string, EventInfo[]>();
  events.forEach((event) => {
    const eventDate = format(event.start, "yyyy-MM-dd");
    if (!eventsMap.has(eventDate)) {
      eventsMap.set(eventDate, []);
    }
    eventsMap.get(eventDate)!.push(event);
  });

  return (
    <table className="w-full table-fixed">
      <thead>
        <tr>
          {weekTitle.map((day) => (
            <th key={day} className="border">
              {day}
            </th>
          ))}
        </tr>
      </thead>
      <tbody>
        {monthGrid.map((week, i) => (
          <tr key={i} style={{ height: `${cellHeight}px` }}>
            {week.map((cell, j) => (
              <td key={j} className="align-top border">
                <div className={cell?.outside ? "text-gray-400" : undefined}>{cell?.day}</div>
                {eventsMap.get(cell.date)?.map((event: EventInfo) => (
                  <div key={event.id} className="event"  onClick={() => eventClick?.(event)}>
                    {eventContent ? eventContent(event) : event.title}
                  </div>
                ))}
              </td>
            ))}
          </tr>
        ))}
      </tbody>
    </table>
  );
};

export default memo(RayCalendarTable);
