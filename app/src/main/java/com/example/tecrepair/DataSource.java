package com.example.tecrepair;

import java.util.ArrayList;

// Datos de ejemplo escritos a mano (no hay base de datos).
// Los favoritos se guardan solo en memoria dentro de cada Tecnico (campo "favorito").
public class DataSource {

    static ArrayList<Tecnico> lista = new ArrayList<>();

    static {
        lista.add(new Tecnico(1, "Smartfix", "Telefonía", "Telefonía",
                new String[]{"Pantalla rota", "Cambio de batería", "Liberación"},
                4.8f, 500, "+56 9 1234 5678", -29.9027, -71.2519,
                "Lun a Sáb: 10:00 - 19:00", true, true, "Efectivo, débito y crédito",
                "Frente a Plaza de Armas, Centro", "Daniel",
                "Un muy buen lugar para reparar mis dispositivos, llevé mi notebook y ahora vuela",
                R.drawable.ic_taller, true));

        lista.add(new Tecnico(2, "Jack's Computer Service", "Computación y notebooks", "Computación",
                new String[]{"Armado de PC", "Formateo", "Cambio de pasta térmica"},
                4.7f, 320, "+56 9 2345 6789", -29.9062, -71.2526,
                "Lun a Vie: 09:30 - 18:30", false, true, "Efectivo y transferencia",
                "Av. Francisco de Aguirre, Centro", "Camila",
                "Me armaron el PC en un día y quedó perfecto",
                R.drawable.ic_taller, false));

        lista.add(new Tecnico(3, "ImpreTec", "Impresoras y tóner", "Impresoras",
                new String[]{"Cabezal tapado", "Recarga de tóner", "Cambio de rodillos"},
                4.5f, 180, "+56 9 3456 7890", -29.9052, -71.2556,
                "Lun a Sáb: 09:00 - 18:00", true, false, "Efectivo y débito",
                "La Recova, Cienfuegos con Cordovez", "Pedro",
                "Arreglaron mi impresora que ya daba por perdida",
                R.drawable.ic_taller, true));

        lista.add(new Tecnico(4, "PC Master", "Computación de escritorio", "Computación",
                new String[]{"Soldadura de placa", "Cambio de fuente", "Upgrade de RAM y SSD"},
                4.9f, 410, "+56 9 4567 8901", -29.9125, -71.2440,
                "Lun a Sáb: 10:00 - 20:00", true, true, "Efectivo, débito, crédito y transferencia",
                "Cerca de Mall Plaza La Serena", "Javiera",
                "Muy profesionales, me explicaron todo el problema",
                R.drawable.ic_taller, true));

        lista.add(new Tecnico(5, "Mobile Center", "Celulares y tablets", "Telefonía",
                new String[]{"Pantalla rota", "Puerto de carga", "Cambio de batería"},
                4.6f, 260, "+56 9 5678 9012", -29.9266, -71.2757,
                "Lun a Dom: 11:00 - 20:00", false, true, "Efectivo y crédito",
                "Av. del Mar, Sector Cuatro Esquinas", "Matías",
                "Me cambiaron la pantalla en 40 minutos",
                R.drawable.ic_taller, false));

        lista.add(new Tecnico(6, "Consola Fix", "Consolas y videojuegos", "Consolas",
                new String[]{"Cambio de pasta térmica", "Joystick con drift", "Lector de discos"},
                4.4f, 95, "+56 9 6789 0123", -29.9079, -71.2805,
                "Mar a Sáb: 12:00 - 19:00", false, false, "Efectivo y transferencia",
                "Faro Monumental, Av. del Mar", "Sofía",
                "Dejaron mi consola como nueva",
                R.drawable.ic_taller, false));
    }

    // Busca un técnico por su id
    static Tecnico buscarPorId(int id) {
        for (Tecnico t : lista) {
            if (t.id == id) {
                return t;
            }
        }
        return lista.get(0);
    }
}
