# Proyecto AYGO - Video Streaming desde S3

## Introducción

Con el gran avance que han tenido las herramientas tecnológicas tenemos la posibilidad de generar millones de datos día a día y poder transferirlos mediante archivos, videos, mensajes, audios, etc. permitiendo una comunicación instantánea y asertiva. El problema de generar tantos datos y tan rápido es que muchas veces no tenemos un sistema centralizado donde podamos consultarlos, lo cual hace que  esta información esté distribuida por múltiples herramientas y su búsqueda sea complicada y prolongada. En el desarrollo de este proyecto proponemos una arquitectura para la centralización de información (videos) relacionados con definiciones de proyectos de tecnología, ya que la falta y/o pérdida de documentación de este tipo de proyectos es común en las grandes empresas.

## Descripción del problema

De acuerdo a la revista “Training Magazine”, las compañías gastan en promedio 62,4 horas entrenando a los empleados, y, en promedio, las compañías gastan $1,207 dólares por empleado que se entrena.
Gran parte de las empresas de tecnología utilizan diferentes herramientas para interactuar en equipo como Teams, Sharepoints, Slack, BlueJeans, Zoom, correo electrónico, etc. Si bien no existe un artículo al respecto, desde la perspectiva del equipo, las empresas en las que laburamos cuentan con un problema de centralización de documentación de los proyectos existentes, la mayoría de los videos están almacenados en las herramientas mencionadas anteriormente y en casi la totalidad de las ocasiones, estos recursos cuentan con accesos restringidos o fueron eliminados lo cual hace más difícil el acceso a esta información.

### Arquitectura de información

Actualmente la empresa tiene los videos repartidos en las diferentes herramientas, solo permite que los videos sean reproducidos a través de la misma herramienta a menos que sean descargados. Con la solución de video streaming de AWS, la empresa podrá tener un sistema de gestión de videos centralizada que permitirá que los recursos sean reproducidos en cualquier lugar del mundo,  y que estos puedan ser reproducidos en cualquier dispositivo.

### Datos

Con la solución se puede tener datos estructurados, ya que se logrará tener un catálogo de videos que se encuentran en la nube con sus respectivos metadatos y que facilita la gestión del conocimiento de la empresa.

### Arquitectura de negocio

Muchos procesos de la empresa se documentan en videos, grabaciones o presentaciones, actualmente estos videos se encuentran en diferentes herramientas y son de difícil búsqueda para los usuarios. Con la solución de video streaming de AWS, la empresa podrá tener un sistema de video streaming centralizado, lo que facilita la gestión del conocimiento de la empresa, esto permitirá llegar a una línea destino donde los procesos serán más formales y documentados, lo que permitirá que la empresa tenga un mejor control de los procesos.

### Seguridad

La solución de video streaming de AWS permite que los videos sean accesibles desde cualquier lugar del mundo pero solo para usuarios autorizados, actualmente algunos videos pueden estar expuestos por mal manejo de permisos , lo que habilita a cualquier persona tener acceso a estos videos. Con la integración de la solución y el uso de Cognito más algún proveedor de identidad, se podrá tener un control de acceso a los videos a empleados autorizados.

## Arquitectura de la solución

<img src="https://lucid.app/publicSegments/view/ed993bc1-8c03-4408-95b4-302f1e6bc601/image.png">


La solución que nosotros proponemos es tener una arquitectura 100% basada en la nube la cual nos ayude a la recolección, análisis y centralización de la información relacionada a definiciones de proyectos de tecnología donde los usuarios van a poder subir los videos y a su vez verlos en streaming. Esta solución se basa principalmente en 3 fuentes de entrada de información. La primer fuente de entrada es una aplicación que le permita a los usuarios subir los videos manualmente al sistema centralizado, la segunda fuente es establecer un MSK que esté en constante monitoreo cuando un video sea subido en Sharepoints específicos para guardarlos en el sistema y como tercera fuente un escáner que va a estar revisando constantemente los videos compartidos por correos electrónicos, canales de slack, equipos de teams, grabaciones de BlueJeans y Zoom los cuales van a pasar por un análisis de inteligencia artificial quien determina si un video está relacionado con la descripción de un proyecto de tecnología por medio de tags para así ser almacenado.
Los principales servicios de Amazon que vamos a utilizar son:
- Amplify (Despliegue del frontend)
- Cognito + Proveedor de Identidad (Autenticación)
- MSK (Consumo de información)
- Rekognition (Análisis de los videos con IA)
- Lambdas (Generación de solicitudes de almacenamiento de videos)
- Api gateway (Integridad y autorización)
- Application load balancer (Balanceador de carga del tráfico a las diferentes instancias)
- EC2 Auto scaling (Escalamiento de las instancias por demanda)
- S3 (Almacenamiento de los videos)
- CloudWatch (Monitoreo de la solución)


## Prototipo

Para el alcance de este proyecto nos vamos a enfocar en la siguiente sección de la arquitectura antes propuesta, donde el usuario va a poder cargar los videos y consumirlos en streaming.
Los principales servicios de Amazon que utiliza el prototipo son:
- Amplify (Despliegue del frontend)
- Cognito (Autenticación)
- Api gateway (Integridad y autorización)
- Application load balancer (Balanceador de carga del tráfico a las diferentes instancias)
- EC2 Auto scaling (Escalamiento de las instancias por demanda)
- S3 (Almacenamiento de los videos)
- CloudWatch (Monitoreo de la solución)


<img src="https://lucid.app/publicSegments/view/467db3d8-c051-4039-8f2e-52984f29bada/image.png">

## CDK
La solución de video streaming de AWS se implementó con el uso de CDK, lo que posibilita que la solución sea fácil de implementar y que esta pueda ser replicada en cualquier región de AWS.
Para ello se creó un stack de CDK que contiene los siguientes recursos:
- VPC con subredes para el acceso público y privado.
- S3 Bucket para almacenar los videos.
- Cloudformation para crear el stack de la solución.
- Cognito para el control de acceso a los videos.
- Amplify para el despliegue de la aplicación web.
- EC2 Instance Type t2.micro para el despliegue de la aplicación Java.
- Security Groups para el acceso a los recursos.
- IAM Roles para el acceso a los recursos.
 - Auto Scaling Group para el escalado de la aplicación Java.
- Integración con HTTP URL para el API Gateway.
 API Gateway.
- Lambda para el procesamiento de los videos.


## Pruebas de Carga

![image](https://user-images.githubusercontent.com/31891276/205422807-8ede9d19-4336-457b-a57d-12173bbeddb2.png)

![image](https://user-images.githubusercontent.com/31891276/205422825-81229866-5e52-4eee-9dc3-4eea11df9611.png)

![image](https://user-images.githubusercontent.com/31891276/205422923-1381c091-1500-4523-ac01-8205505b9370.png)

![image](https://user-images.githubusercontent.com/31891276/205422930-90e84748-32fa-43df-830d-8149274107ac.png)

![image](https://user-images.githubusercontent.com/31891276/205422940-91deb247-ee49-4f04-a499-165399792360.png)

## Demo

![image](https://user-images.githubusercontent.com/31891276/205425414-3d0f302b-998c-4cad-8839-ab7ee05018c3.png)



## Conclusión


## Referencias

- Amazon Rekognition. https://aws.amazon.com/es/rekognition 
- 2022 Training Industry Report. https://trainingmag.com/2022-training-industry-report 
- Amazon MSK. https://aws.amazon.com/es/msk 
- API Poller. https://aws.amazon.com/blogs/compute/building-an-api-poller-with-aws-step-functions-and-aws-lambda
- CDK https://docs.aws.amazon.com/cdk/index.html
