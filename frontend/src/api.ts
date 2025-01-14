export const ROOT_PATH = "http://localhost:9000";

export async function getCalenderData(startStr: string, endStr: string, teachers: string[], students: string[]) {
  const params = new URLSearchParams({
    startStr,
    endStr,
    teachers: teachers.join(","),
    students: students.join(","),
  }).toString();
  return await fetch(`${ROOT_PATH}/data?${params}`)
    .then((response) => response.json())
    .then((data) => {
      const events = data.data;
      events.forEach((event: any) => {
        event.title = event.courseName;
        event.resource = event.classroom;
        const date = new Date(Number(event.courseDate));
        event.start = date.toISOString();
        event.end = date.toISOString();
        event.allDay = true;
      });
      return events;
    })
    .catch((error) => {
      console.error("Error:", error);
      return [];
    });
}

export async function getAllTeachers() {
  return await fetch(`${ROOT_PATH}/teacher`)
    .then((response) => response.json())
    .then((data) => {
      return data.data;
    })
    .catch((error) => console.error("Error:", error));
}

export async function getAllStudents() {
  return await fetch(`${ROOT_PATH}/student`)
    .then((response) => response.json())
    .then((data) => {
      return data.data;
    })
    .catch((error) => console.error("Error:", error));
}

export interface ClassroomType {
  id: number;
  name: string;
  size: number;
  software?: string | undefined;
}

export async function getClassroom(): Promise<ClassroomType[]> {
  return await fetch(`${ROOT_PATH}/classroom`)
    .then((response) => response.json())
    .then((data) => {
      return data.data;
    })
    .catch((error) => console.error("Error:", error));
}

export async function saveClassroom(data: ClassroomType) {
  return await fetch(`${ROOT_PATH}/classroom`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(data),
  })
    .then((response) => response.json())
    .then((data) => {
      return data.data;
    })
    .catch((error) => console.error("Error:", error));
}

export async function deleteClassroom(id: number) {
  return await fetch(`${ROOT_PATH}/classroom/${id}`, {
    method: "DELETE",
  })
    .then((response) => response.json())
    .then((data) => {
      return data.data;
    })
    .catch((error) => console.error("Error:", error));
}

export async function getUser(): Promise<boolean> {
  return await fetch(`${ROOT_PATH}/login`)
    .then((response) => response.json())
    .catch((error) => console.error("Error:", error));
}

export const uploadExcel = async (file: File) => {
  const formData = new FormData();
  formData.append("file", file);

  const response = await fetch(`${ROOT_PATH}/upload`, {
    method: "POST",
    body: formData,
  });

  if (!response.ok) {
    throw new Error("Upload failed");
  }

  return response.json();
};
