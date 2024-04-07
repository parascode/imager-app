import React, { useEffect, useState } from "react";
import axios from "axios";

function ImageUploader() {
  const [selectedFile, setSelectedFile] = useState(null);
  const [files, setFiles] = useState([]);
  const [message, setMessage] = useState(null);
  const [loading, setLoading] = useState(false);
  const [uploadedSrc, setUploadedSrc] = useState("");

  useEffect(() => {
    loadFiles();
  }, []);

  const loadFiles = () => {
    const url = "http://localhost:8080/api/v1/s3";
    axios
      .get(url)
      .then((response) => {
        console.log("files");
        console.log(response.data);
        setFiles(response.data);
      })
      .catch((error) => {
        console.log(error.message);
      });
  };

  const formSubmit = (event) => {
    event.preventDefault();
    if (selectedFile != null) {
      //   fileUploader();
      uploadImageToServer();
    } else {
      alert("Select image first!!");
    }
  };
  const handleFileChange = (event) => {
    const file = event.target.files[0];
    console.log(file);
    if (file.type === "image/jpeg" || file.type === "image/png") {
      setSelectedFile(file);
      //   uploadImageToServer();
    } else {
      alert("Select images only!!");
      setSelectedFile(null);
    }
  };
  //   for uploading image to our server
  const uploadImageToServer = () => {
    const url = "http://localhost:8080/api/v1/s3";
    const data = new FormData();
    data.append("file", selectedFile);
    setLoading(true);
    axios
      .post(url, data)
      .then((response) => {
        console.log(response.data);
        setUploadedSrc(response.data);
        setMessage(true);
      })
      .catch((error) => {
        console.log(error);
      })
      .finally(() => {
        console.log("upload image function finished");
        setLoading(false);
      });
  };

  return (
    <div className="main flex justify-center">
      <div className="card rounded shadow w-1/3 m-4 p-4 bg-gray-100">
        <h1 className="text-2xl text-center">Image Uploader</h1>
        <div>
          <form action="" onSubmit={formSubmit}>
            <div className="field_container flex flex-col mt-2">
              <label htmlFor="file">Choose Image</label>
              <input onChange={handleFileChange} type="file" id="file" />
            </div>
            <div className="field_container text-center mt-2">
              <button
                type="submit"
                className="px-3  py-1 bg-blue-600 hover:bg-blue-500 rounded text-white"
              >
                Upload
              </button>
              <button
                type="reset"
                className="ml-2 px-3 py-1 bg-orange-600 hover:bg-orange-500 rounded text-white"
              >
                Clear
              </button>
            </div>
          </form>
        </div>

        {loading && (
          <div>
            <h1 className="text-center">Image is being uploaded!!</h1>
            <div className="flex justify-center items-center">
              <div className="loader ease-linear rounded-full border-8 border-t-8 border-gray-200 h-12 w-12 animate-spin"></div>
            </div>
          </div>
        )}
        {/* success message */}
        {message && (
          <div
            className="my-3 bg-green-100 border border-green-400 text-green-700 px-4 py-3 rounded relative"
            role="alert"
          >
            <strong className="font-bold">
              Success! Your Image is uploaded!
            </strong>
            <span className="block sm:inline">{message}</span>
            <span className="absolute top-0 bottom-0 right-0 px-4 py-3">
              <svg
                className="fill-current h-6 w-6 text-green-500"
                role="button"
                xmlns="http://www.w3.org/2000/svg"
                viewBox="0 0 20 20"
              >
                <title>Close</title>
                <path d="M14.348 5.652a.5.5 0 0 1 .708.708L10.707 10l4.35 4.35a.5.5 0 0 1-.708.708L10 10.707l-4.35 4.35a.5.5 0 0 1-.708-.708L9.293 10 4.943 5.65a.5.5 0 0 1 .708-.708L10 9.293l4.35-4.35z" />
              </svg>
            </span>
          </div>
        )}

        {/* showing uploaded image */}
        <div className="uploaded_image mt-3 flex justify-center">
          <img
            src={uploadedSrc}
            alt=""
            className="rounded shadow w-1/2 h-auto"
          />
        </div>
      </div>
      <div>
        {files.map((item) => {
          <img src={item} alt="" key={item} />;
        })}
      </div>
    </div>
  );
}

export default ImageUploader;
