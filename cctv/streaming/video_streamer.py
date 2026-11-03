class VideoStreamer:
	def __init__(self, camera, conn):
		self.camera = camera
		self.conn = conn
	
	def stream(self):
		if not self.camera.isOpened():
			return None
	
		print('Streaming...')
		ret, frame = capture_frame(self.camera)
		
		if not ret:
			return
		
		ret, buffer = cv2.imencode('.jpg', frame)
		
		if not ret:
			return
		
		frame_bytes = buffer.tobytes()
		
		self.conn.sendall(len(frame_bytes).to_bytes(4, "big"))
		self.conn.sendall(frame_bytes)
	
	def close(self):
		self.camera.release()
		self.conn.send("closed\n".encode())
		
	def stop(self):
		ret, frame = capture_frame(self.camera)
		ret, buffer = cv2.imencode('.jpg', frame)
		
		frame_bytes = buffer.tobytes()
		self.conn.sendall(len(frame_bytes).to_bytes(4, "big"))
		self.conn.sendall(frame_bytes)
		self.conn.send('Camera stopped\n'.encode())