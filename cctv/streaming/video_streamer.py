import cv2

class VideoStreamer:
	def __init__(self, camera, conn):
		self.camera = camera
		self.conn = conn
	
	def stream(self):
		if not self.camera.is_opened():
			return None
	
		print('Streaming...')
		
		frame = self.camera.capture_frame()
		frame_bytes = self.camera.encode_frame(frame)
		
		self.conn.sendall(len(frame_bytes).to_bytes(4, "big"))
		self.conn.sendall(frame_bytes)
		
	def stop(self):
		frame = self.camera.capture_frame()
		frame_bytes = self.camera.encode_frame(frame)
		
		self.conn.sendall(len(frame_bytes).to_bytes(4, "big"))
		self.conn.sendall(frame_bytes)
		self.conn.send('Camera stopped\n'.encode())