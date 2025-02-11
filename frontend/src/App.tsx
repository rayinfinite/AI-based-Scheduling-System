import { useEffect, useState } from "react";
import MyRayCalendar from "./MyRayCalendar";
import UploadPage from "./UploadPage";
import { Button } from "./components/ui/button";
import { getJwtToken, getUser, getUserToken, ROOT_PATH } from "./api";
import { LogIn } from "lucide-react";

function App() {
  const [data, setData] = useState<string>("false");
  const [name, setName] = useState<string>("");

  useEffect(() => {
    getUser().then((data) => {
      setData(String(data));
      getJwtToken();
    });
  }, []);

  useEffect(() => {
    if (data) {
      getUserToken().then((token) => {
        setName(token.name);
        getJwtToken();
      });
    }
  }, [data]);

  const login = async () => {
    const data = await getUser();
    if (!data) {
      window.location.href = `${ROOT_PATH}/oauth2/authorization/azure`;
    } else {
      getJwtToken();
    }
  };

  return (
    <div className="px-2 sm:px-6 ">
      <div className="flex flex-col items-center justify-between my-5 lg:flex-row">
        <h1>AI Course Scheduling System</h1>
        {data != "false" ? (
          <span>Hello {name}</span>
        ) : (
          <Button onClick={login}>
            <LogIn className="w-4 h-4" />
            Login
          </Button>
        )}
      </div>
      {data == "true" && <UploadPage />}
      <MyRayCalendar />
    </div>
  );
}

export default App;
