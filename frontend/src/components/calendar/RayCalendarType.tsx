import { ReactNode } from "react";

export type EventInfo = {
  id: number;
  title: string;
  start: Date;
  end: Date;
} & {
  [key: string]: ReactNode;
};

export interface RayCalendarProps {
  weekStartsOn?: number; // 0 for Sunday, 1 for Monday, etc.
  showOutsideDays?: boolean; // Show days from the previous and next month
  fixedWeeks?: boolean; // Always show 6 weeks
  fetchEvents: (start: Date, end: Date, timeZone: string) => Promise<EventInfo[]>;
  eventContent?: (event: EventInfo) => JSX.Element;
  eventClick?: (event: EventInfo) => void;
  resourceHeader?: string; // Header for the resource column
  resources?: string[]; // List of resources
  leftTitle?: ReactNode;
  rightTitle?: ReactNode;
}

export interface CalendarApi {
  title: string;
  date: Date;
  view: string;
  getDate: () => Date;
  gotoDate: (date: Date) => void;
  today: () => void;
  prev: () => void;
  next: () => void;
  prevYear: () => void;
  nextYear: () => void;
  incrementDays: (duration: number) => void;
  changeView: (value: string) => void;
  refreshEvents?: () => void;
}
