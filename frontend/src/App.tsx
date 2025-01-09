import MyCalendar from './MyCalender';
import UploadPage from './UploadPage';

function App() {
  return (
    <div className="px-2 sm:px-6">
      <h1 className='my-5'>AI Course Scheduling System</h1>
      <UploadPage />
      <MyCalendar />
    </div>
  );
}

export default App
