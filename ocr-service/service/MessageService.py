import os
import pika
import time


class MessageService:
    def __init__(self, host, port, username, password):
        credentials = pika.PlainCredentials(
            username=username, 
            password=password
        )
        self.connection = self._connect(host, port, credentials)
        self.channel = self.connection.channel()

        input_queue = os.getenv("OCR_INPUT")
        output_queue = os.getenv("OCR_OUTPUT")

        self.channel.queue_declare(queue=input_queue, durable=True)
        self.channel.queue_declare(queue=output_queue, durable=True)

    def _connect(self, host, port, credentials):
        for _ in range(10):
            try:
                connection = pika.BlockingConnection(
                    pika.ConnectionParameters(
                        host=host,
                        port=port,
                        credentials=credentials
                    )
                )
                return connection
            except pika.exceptions.AMQPConnectionError:
                print("RabbitMQ not ready, retrying...")
                time.sleep(2)
        else:
            raise RuntimeError("RabbitMQ unavailable")

    def consume(self, queueName, callback):

        self.channel.basic_consume(
            queue=queueName,
            on_message_callback=callback,
            auto_ack=True
        )

        print(' [*] Waiting for messages...')
        self.channel.start_consuming()

    def publish(self, queueName, message):
        self.channel.basic_publish(
            exchange='',
            routing_key=queueName,
            body=message,
            properties=pika.BasicProperties(
                delivery_mode=2
            )
        )
