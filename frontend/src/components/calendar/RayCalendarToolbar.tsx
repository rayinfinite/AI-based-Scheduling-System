import { Button } from "../ui/button";
import { ChevronLeft, ChevronRight } from "lucide-react";
import { DatePicker } from "../date-picker";
import { CalendarApi } from "./RayCalendarType";
import { Tabs, TabsList, TabsTrigger } from "../ui/tabs";
import { memo, ReactNode } from "react";

const viewOptions = [
  { label: "Month", value: "Month" },
  { label: "Week", value: "Resource" },
  // { label: "Day", value: "timeGridDay" },
];

const RayCalendarToolbar = ({
  calendarApi,
  leftTitle,
  rightTitle,
}: {
  calendarApi: CalendarApi;
  leftTitle: ReactNode;
  rightTitle: ReactNode;
}) => {
  return (
    <div className="space-y-4">
      <div className="grid items-center grid-cols-1 gap-4 py-5 lg:grid-cols-3">
        <div className="flex items-center justify-center gap-4 lg:justify-start">
          <Button variant="default" onClick={() => calendarApi.today()}>
            Today
          </Button>
          <DatePicker onChange={(date) => calendarApi.gotoDate(date)} />
          {leftTitle}
        </div>

        <div className="flex items-center justify-center gap-4">
          <Button className="rounded-full [&_svg]:size-6" size="icon" onClick={() => calendarApi.prev()}>
            <ChevronLeft />
          </Button>
          <h1 className="min-w-[240px] text-center">{calendarApi.title}</h1>
          <Button className="rounded-full [&_svg]:size-6" size="icon" onClick={() => calendarApi.next()}>
            <ChevronRight />
          </Button>
        </div>

        <div className="flex items-center justify-center gap-4 lg:justify-end">
          <Tabs value={calendarApi.view} onValueChange={calendarApi.changeView} className="flex gap-2">
            <TabsList>
              {viewOptions.map((option) => (
                <TabsTrigger
                  key={option.value}
                  value={option.value}
                  onClick={() => calendarApi.changeView(option.value)}
                >
                  {option.label}
                </TabsTrigger>
              ))}
            </TabsList>
          </Tabs>
          {rightTitle}
        </div>
      </div>
    </div>
  );
};

export default memo(RayCalendarToolbar);
