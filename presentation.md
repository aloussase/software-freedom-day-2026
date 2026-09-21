---
author: Alexander Goussas
---

# Modelado de Dominio con Tagless Final

---

## Whoami

- Desarrollador de software senior en Thoughtworks
- Actualmente trabajando en una solución de shopping para una aerolínea
- Más experiencia con Java+Spring Boot
- Interés en programación funcional, compiladores, arquitectura y calidad de
  software

---

## Modelando un dominio

> In the field of computer science a conceptual model aims to express the
> meaning of terms and concepts used by domain experts ... and is explicitely
> chosen to be independent of implementation

En computación un modelo conceptual expresa el significado de términos y
conceptos utilizados por expertos del dominio ... y es escogido para ser
independiente de la implementación

* https://en.wikipedia.org/wiki/Domain_model

---

### Ejemplos de dominios

- eCommerce
- Sistemas de lealtad
- Banca
- Booking de hoteles
- Soporte a usuarios

---

### Subdominios

- Carrito
- Checkout
- Vitrina
- Busca
- Detalles del producto

---

## _Domain Specific Languages_

Un _Domain Specific Language_ (DSL) es un lenguaje de programación especializado
para un dominio particular. 

El objetivo es expresar un problema o solución de una forma más clara de lo que
un lenguaje existente lo permitiría.

---

Algunos ejemplos son:

- SQL
- AWK, shell scripts
- CSS
- Yacc
- Gherkin
- Gradle

---

### DSLs embebidos (eDSL)

- Macros en Lisp (permiten la creación de eDSLs)
- Sobrecarga de operadores
- API de Streams en Java 
- LINQ en .NET

---

## Tagless Final

> To interpret a typed object language in a typed metalanguage without tagging
or type system extensions

Tagless Final es un patrón de diseño que tiene por objetivo interpretar un
lenguaje tipado en un metalenguaje tipado sin etiquetas o extensiones.

* Descrito en el paper https://okmij.org/ftp/tagless-final/index.html

---

## El lenguaje

```haskell
class Symantics repr where
  int :: Int -> repr Int
  bool :: Bool -> repr Bool
  lam :: (repr a -> repr b) -> repr (a -> b)
  app :: repr (a -> b) -> repr a -> repr b
```

---

Una implementación

```haskell
data R x = R x deriving Show

instance Symantics R where
  int = R
  bool = R
  lam f = R (\a ->
      case f (R a) of
        R b -> b
    )
  app (R f) (R x) = R (f x)
```

---

Una prueba

```haskell
test1 :: Symantics repr => repr Bool
test1 = app (lam id) (bool True)

main :: IO ()
main = do
  print @(R Bool) test1
```

---

Podemos crear una implementación alternativa

```haskell
data LogR repr a = LogR (IdentityT IO (repr a))

instance (Symantics repr) => Symantics (LogR repr) where
  int n = LogR (runIdentityT (liftIO (putStrLn "Making an int") >> return (int @repr n)))
  bool b = LogR (runIdentityT (liftIO (putStrLn "Making a bool") >> return (bool @repr b)))
  -- app and lam left as exercises for the reader
```

---

Y ejecutar otra prueba

```haskell
main :: IO ()
main = do
  case int @(LogR Maybe) 42 of
    LogR (IdentityT x) -> x
  return ()
```

---

## ¿Qué hemos conseguido?

- Codificar un DSL en un metalenguaje (Haskell) de forma que se preserve la
  seguridad de tipos.
- La sintaxis de nuestro lenguaje está dada por su interfaz y la semántica por
  las implementaciones concretas.
- Solo con leer la definición de la interfaz sabemos sabe cuál es el
  propósito del programa.
- Las implementaciones son intercambiables (principio de sustitución Liskov y
  principio de inversión de dependencias).

---

## Ejemplo más práctico

```java
public record Vitrine(
        String id,
        String name,
        List<VItem> items
) {
    public record VItem(
            String id,
            String name,
            BigDecimal price
    ) {
    }
}
```

---

## Patrón newtype 

- Valerse del sistema de tipos para garantizar correctitud
- Los records aún no soportan constructores primarios privados
- Proyecto Valhalla haría de esto una _zero-cost abstraction_

```java
public record VitrineId(String id) {
}
```

```java
public record Vitrine(
        VitrineId id,
        String name,
        List<VItem> items
) {
    public record VItem(
            String id,
            String name,
            BigDecimal price
    ) {
    }
}
```

---

## Nuestra sintaxis

```java
public interface VitrineSymantics {

    List<Vitrine> homeVitrines();

    List<Vitrine> vitrines();

}
```

---

## Diferentes semánticas

---

```java
public class VendorVitrines implements VitrineSymantics {
    private final HttpClient client;

    public VendorVitrines(HttpClient client) {
        this.client = client;
    }

    @Override
    public List<Vitrine> homeVitrines() {
        // .....
        return List.of();
    }

    @Override
    public List<Vitrine> vitrines() {
        // ....
        return List.of();
    }
}
```

---

```java
public class InMemoryVitrines implements VitrineSymantics {
    private final ConcurrentHashMap<VitrineId, Vitrine> vitrines = new ConcurrentHashMap<>();
    
    @Override
    public List<Vitrine> homeVitrines() {
        return List.of();
    }

    @Override
    public List<Vitrine> vitrines() {
        return List.of();
    }
}
```

---

## Ventajas de desarrollar software de esta forma

- Evitar el vendor lock-in
- Facilita los tests unitarios
- Permite a diferentes equipos trabajar de forma independiente

---

## Palabras finales

_Así como contemplamos una obra de arte, nos maravillamos ante la magnificencia
de un edificio y nos sobrecogemos al ver una actuación fantástica, el software
también puede ser hermoso._

---

## Accede al código de ejemplo y presentación

```bash
chafa --scale max ~/Developer/software-freedom-day-2026/assets/code.png
```
