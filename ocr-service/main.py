import json
import os

from service.MessageService import MessageService
from service.MinioService import MinioService
from service.OcrService import OcrService

LOCAL_PATH = "tmp/"
BUCKET_NAME = os.getenv("MINIO_BUCKET")

def callback(channel, method, properties, body):
    message = json.loads(body.decode())
    print(f"[x] Received: {message}")

    imageName = message["image_url"]
    path = LOCAL_PATH + imageName

    if not minioService.getFile(BUCKET_NAME, imageName, path):
        messageService.publish({
            "status": "FAILED",
            "task_id": message["task_id"]
        })
        return

    print(" [x] File downloaded")

    text = ocrService.recognize(path)

    response = {
        "task_id": message["task_id"],
        "status": "SUCCESS",
        "result": text
    }

    messageService.publish(
        os.getenv("OCR_OUTPUT"),
        json.dumps(response)
    )
    print("[x] Published result:", response)

if __name__ == "__main__":
    minioService = MinioService(
        os.getenv("MINIO_URL"),
        os.getenv("MINIO_ACCESS_KEY"),
        os.getenv("MINIO_SECRET_KEY")
    )
    ocrService = OcrService()
    messageService = MessageService(
        os.getenv("RABBITMQ_HOST"),
        os.getenv("RABBITMQ_PORT"),
        os.getenv("RABBITMQ_USER"),
        os.getenv("RABBITMQ_PASSWORD")
    )

    messageService.consume(os.getenv("OCR_INPUT"), callback)