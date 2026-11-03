import cv2

class VideoStreamer:
	def __init__(self, camera, conn, camera_capture):
		self.camera = camera
		self.conn = conn
		self.capture = camera_capture
	
	def stream(self):
		if not self.camera.isOpened():
			return None
	
		print('Streaming...')
		ret, frame = self.capture.capture_frame()
		
		if not ret:
			return
		
		ret, buffer = cv2.imencode('.jpg', frame)
		
		if not ret:
			return
		
		frame_bytes = buffer.tobytes()
		
		self.conn.sendall(len(frame_bytes).to_bytes(4, "big"))
		self.conn.sendall(frame_bytes)
		
	def stop(self):
		ret, frame = self.capture.capture_frame()
		
		if not ret:
			return
		
		ret, buffer = cv2.imencode('.jpg', frame)
		
		if not ret:
			return
		
		frame_bytes = buffer.tobytes()
		self.conn.sendall(len(frame_bytes).to_bytes(4, "big"))
		self.conn.sendall(frame_bytes)
		self.conn.send('Camera stopped\n'.encode())