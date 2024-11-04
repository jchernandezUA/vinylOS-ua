
# VynilOS

# Descripción
VinylOS es una aplicación móvil diseñada para la gestión y exploración de colecciones musicales. Permite a los usuarios consultar y navegar a través de un amplio catálogo de álbumes, artistas y coleccionistas. La aplicación ofrece funcionalidades para obtener información detallada sobre cada álbum, artista y coleccionista, así como para agregar nuevos álbumes y asociar tracks específicos con ellos. VinylOS se enfoca en proporcionar una experiencia de usuario intuitiva y fluida, facilitando la organización y el acceso a la colección musical de manera eficiente y agradable.Funcionalidades Core

Compatibilidad Android (Lollipop) API 21+

## Setup

### Prerrequisitos
Asegúrate de tener instalados los siguientes requisitos en tu máquina antes de comenzar:

* Descargar **repositorio**
* **Java Development Kit (JDK)**: JDK 17 o superior.
* **Android SDK**: Asegúrate de tener instalada la versión del SDK que usa la app (API 33 o la que corresponda).
* Instalar Android Studio Koala Feature Drop | 2024.1.2  desde [aquí](https://developer.android.com/studio?hl=es-419), puede ser para MacOS, Windows o Linux
* Emulador compatible o dispositivo físico

### Pasos para Ejecutar la App en Android Studio
* Abre Android Studio.
* Selecciona File > Open y navega hasta el directorio del repositorio clonado.
* Selecciona la carpeta del proyecto y haz clic en Open.

**Configurar el SDK**
Android Studio debería detectar automáticamente la versión del SDK que necesitas. Si no está instalada, ve a File > Project Structure > Project y selecciona la versión correcta o instala la que falte en el SDK Manager (puedes acceder desde el menú superior: Tools > SDK Manager).
* Sincronizar las dependencias del proyecto
* Una vez abierto el proyecto, Android Studio debería sincronizar automáticamente las dependencias de Gradle. Si no lo hace, selecciona File > Sync Project with Gradle Files.
* Ejecutar la app en un emulador o dispositivo físico

**Para Emulador** Configura un dispositivo virtual desde Device Manager en Android Studio:
* Abre Device Manager (en la esquina superior derecha).
* Haz clic en Create Device, elige un perfil (por ejemplo, Pixel 4a) y la versión de Android que corresponda (API 21+).
* Haz clic en Next y luego en Finish para configurar el emulador.

**Para un Dispositivo Físico:**
* Activa Depuración USB en el dispositivo.
* Conecta el dispositivo a tu computadora y selecciona "Permit Debugging" si se solicita.
* Construir y ejecutar la app

* En Android Studio, selecciona la opción Run > Run 'app' o haz clic en el botón de play (▶️) en la barra superior.
* Elige tu emulador o dispositivo físico en la lista.
* Android Studio compilará el proyecto y, cuando esté listo, lanzará la app en el dispositivo seleccionado.
* No requiere backend porque está apuntando a un ambiente de [QA](https://back-vinylos-e11-da7f5dfc1c26.herokuapp.com/)


### Pruebas

El proyecto cuenta con pruebas unitarias y E2E para compose, se pueden correr las pruebas desde AS dando click derecho en la carpeta de androidTest y correr o desde la consola con el siguiente comando
`
./gradlew connectedAndroidTest
`