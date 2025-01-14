import { format } from "date-fns";
import { EventInfo } from "./RayCalendarType";
import { memo } from "react";

interface RayCalendarResourceTableProps {
  resourceHeader: string;
  resources: string[];
  weekTitle: string[];
  weekGrid: { day: number; outside: boolean; date: string }[][];
  cellHeight: number;
  events: EventInfo[];
  eventContent?: (event: EventInfo) => JSX.Element;
  eventClick?: (event: EventInfo) => void;
}

const RayCalendarResourceTable: React.FC<RayCalendarResourceTableProps> = ({
  resourceHeader,
  resources,
  weekTitle,
  weekGrid,
  cellHeight,
  events,
  eventContent,
  eventClick,
}) => {
  const eventsMap = new Map<string, EventInfo[]>();
  events.forEach((event) => {
    const eventDate = format(event.start, "yyyy-MM-dd") + event.resource;
    if (!eventsMap.has(eventDate)) {
      eventsMap.set(eventDate, []);
    }
    eventsMap.get(eventDate)!.push(event);
  });

  return (
    <table className="w-full table-fixed">
      <thead>
        <tr>
          <th className="w-24 border">{resourceHeader}</th>
          {weekTitle.map((day) => (
            <th key={day} className="border">
              {day}
            </th>
          ))}
        </tr>
      </thead>
      <tbody>
        {resources.map((resource) => (
          <tr key={resource} style={{ height: `${cellHeight}px` }}>
            <td className="px-2 border">{resource}</td>
            {weekGrid[0].map((cell, j) => (
              <td key={j} className="align-top border">
                {eventsMap.get(cell.date + resource)?.map((event: EventInfo) => (
                  <div key={event.id} className="event" onClick={() => eventClick?.(event)}>
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

export default memo(RayCalendarResourceTable);
