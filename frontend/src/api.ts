export const ROOT_PATH = process.env.NODE_ENV === "production" ? "" : "http://localhost:9000";

const getCsrfToken = () => {
  const csrfToken = document.cookie.match("(^|;)\\s*XSRF-TOKEN\\s*=\\s*([^;]+)")?.pop();
  return csrfToken || "";
};

export default function request(url: string, options: RequestInit = {}) {
  const headers: Record<string, string> = options.headers ? { ...(options.headers as Record<string, string>) } : {};
  if (options.method === "POST" && !(options.body instanceof FormData)) {
    headers["Content-Type"] = headers["Content-Type"] || "application/json";
  }

  return fetch(url, {
    ...options,
    headers: {
      ...headers,
      "X-XSRF-Token": getCsrfToken(),
      Authorization: `Bearer ${localStorage.getItem("token") || ""}`,
    },
  })
    .then((response) => {
      if (response.status == 401) {
        window.location.href = `${ROOT_PATH}/oauth2/authorization/azure`;
      }
      if (!response.ok) {
        throw new Error(`网络请求失败，状态码：${response.status}`);
      }
      const contentType = response.headers.get("content-type");
      if (contentType && contentType.includes("application/json")) {
        return response.json();
      }
      return response;
    })
    .then((data) => {
      return data.data || data;
    })
    .catch((error) => {
      console.error("Error:", error);
      throw error;
    });
}

export async function getCalenderData(startStr: string, endStr: string, teachers: string[], students: string[]) {
  const params = new URLSearchParams({
    startStr,
    endStr,
    teachers: teachers.join(","),
    students: students.join(","),
  }).toString();
  return await request(`${ROOT_PATH}/data?${params}`)
    .then((data) => {
      const events = data;
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
  return await request(`${ROOT_PATH}/teacher`);
}

export async function getAllStudents() {
  return await request(`${ROOT_PATH}/student`);
}

export interface ClassroomType {
  id: number;
  name: string;
  size: number;
  software?: string | undefined;
}

export async function getClassroom(): Promise<ClassroomType[]> {
  return await request(`${ROOT_PATH}/classroom`);
}

export async function saveClassroom(data: ClassroomType) {
  return await request(`${ROOT_PATH}/classroom`, {
    method: "POST",
    body: JSON.stringify(data),
  });
}

export async function deleteClassroom(id: number) {
  return await request(`${ROOT_PATH}/classroom/${id}`, {
    method: "DELETE",
  });
}

export async function getClassname(): Promise<string[]> {
  return await request(`${ROOT_PATH}/classname`);
}

export const uploadExcel = async (file: File) => {
  const formData = new FormData();
  formData.append("file", file);

  return await request(`${ROOT_PATH}/upload`, {
    method: "POST",
    body: formData,
  });
};

export const uploadResultExcel = async (file: File) => {
  const formData = new FormData();
  formData.append("file", file);

  return await request(`${ROOT_PATH}/resultUpload`, {
    method: "POST",
    body: formData,
  });
};

export const downloadExcel = async () => {
  const response = await request(`${ROOT_PATH}/download`);

  if (!response.ok) {
    throw new Error("Download failed");
  }

  const blob = await response.blob();
  const url = window.URL.createObjectURL(blob);
  const a = document.createElement("a");
  a.href = url;
  a.download = "Course.xlsx";
  document.body.appendChild(a);
  a.click();
  a.remove();
  window.URL.revokeObjectURL(url);
};

export async function getUser(): Promise<string> {
  return await fetch(`${ROOT_PATH}/login`)
    .then((response) => response.json())
    .catch((error) => console.error("Error:", error));
}

export async function getUserToken() {
  return await fetch(`${ROOT_PATH}/login/token_details`)
    .then((response) => response.json())
    .catch((error) => console.error("Error:", error));
}

export async function getJwtToken() {
  return await fetch(`${ROOT_PATH}/login/jwt`)
    .then((response) => response.json())
    .then((data) => {
      localStorage.setItem("token", data.data);
    })
    .catch((error) => console.error("Error:", error));
}
