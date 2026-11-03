import uuid
import requests
import socket
import cv2
import os
from datetime import datetime
from dotenv import load_dotenv

class Camera:
	def __init__(self, name, type="IP", version="1.0"):
		self.id = str(uuid.uuid4())
		self.capture = cv2.VideoCapture(0)
		
		if not self.capture.isOpened():
			raise RuntimeError("Could not initialize camera.")
		
		load_dotenv()
		
		host = socket.gethostname()
		self.ip = socket.gethostbyname(host)
		self.installation_date = str(datetime.now())
		self.name = name
		self.type = type
		self.version = version
		self.status = self.get_status()
		
		try:
			data = requests.get("https://ipapi.co/json/", timeout=5).json()

			city = data['city']
			region = data['region']
			country = data['country']
			
			self.location = city + " ," + region + " ," + country
			
		except Exception:
			#set a default location
			self.location = os.getenv("LOCATION", "unknown")
			
			
	def __str__(self):
		return f"Camera {self.id} {self.name} {self.type} {self.version} {self.status}"
		
	def close(self):
		self.capture.release()
		self.capture = None
	
	def is_opened(self):
		return self.capture is not None and self.capture.isOpened()
	
	def open(self):
		if not self.capture or not self.capture.isOpened():
			self.capture = cv2.VideoCapture(0)
			
			if not self.capture.isOpened():
				raise RuntimeError("Could not initialize camera.")
			
	def capture_frame(self):
		if self.is_opened():
			ret, frame = self.capture.read()
			
			if not ret:
				raise RuntimeError("Error occured while capturing the frame.")
			
			return frame
		
		raise RuntimeError("Error occured while capturing the frame.")
		
	def capture_image(self):
		if self.is_opened():
			ret, frame = self.capture.read()
			
			if not ret:
				return
				
			now = datetime.now().strftime("%Y_%m_%d_%H_%M_%S")

			image_title = "IW_CCTV_Snapshot_" + now + ".jpg"
			captured = cv2.imwrite(image_title, frame)
			
			return captured
			
		else:
			raise RuntimeError("Error capturing the frame, please make sure the camera is opened.")
		
	def get_status(self):
		return "online" if self.is_opened() else "offline"
	
	def release(self):
		if self.is_opened():
			self.capture.release()
			
	def encode_frame(self, frame):
		ret, buffer = cv2.imencode('.jpg', frame)
	
		if not ret:
			raise RuntimeError("Error encoding frame.")
		
		return buffer.tobytes()
	
	def get_info(self):
		return {
			"id": self.id,
			"name": self.name,
			"type": self.type,
			"version": self.version,
			"installation_date": self.installation_date,
			"location": self.location,
			"status": self.get_status(),
			"ip": self.ip,
		}