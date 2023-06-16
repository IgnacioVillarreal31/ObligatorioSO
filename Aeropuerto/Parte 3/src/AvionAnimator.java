import java.util.Random;
import java.util.concurrent.Semaphore;

public class AvionAnimator implements Runnable {

    private final Avion avion;
    private final int maxX, maxY;
    private final Random rnd;
    private boolean moveRight = true, moveDown = true;
    private static final int STEP = 1, WAIT = 40;

    private int posicion = 0;
    private Semaphore recorrer;

    public AvionAnimator(Avion avion, int maxX, int maxY, int i, Semaphore rec) {
        this.avion = avion;
        this.maxX = maxX;
        this.maxY = maxY;
        rnd = new Random();
        avion.setX(rnd.nextInt(maxX - avion.getSize()));
        avion.setY(rnd.nextInt(maxY - avion.getSize()));
        recorrer = rec;
        new Thread(this, "Pelota " + String.valueOf(i)).start();
    }

    @Override
    public void run() {
/*
        while (true) {

            int dx = moveRight ? STEP : -STEP;
            int dy = moveDown ? STEP : -STEP;

            int newX = ball.getX() + dx;
            int newY = ball.getY() + dy;

            if (newX + ball.getSize() >= maxX || newX <= 0) {
                newX = ball.getX() - dx;
                moveRight = !moveRight;
                System.out.println("Thread actual choco: " + Thread.currentThread().getName());
                synchronized (this) {
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            if (newY + ball.getSize() >= maxY || newY <= 0) {
                newY = ball.getY() - dy;
                moveDown = !moveDown;
                System.out.println("Thread actual choco: " + Thread.currentThread().getName());

                synchronized (this) {
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

            }

            ball.setX(newX);
            ball.setY(newY);

            try {
                //System.out.println("Thread actual: " + Thread.currentThread().getName());
                Thread.sleep(WAIT);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
*/

        boolean continuar = false;
        while (true) {
            if (avion.getMoving()) {
                if (avion.getX() < avion.getTargetX()) {
                    avion.setX(avion.getX() + 1);
                } else if (avion.getX() > avion.getTargetX()) {
                    avion.setX(avion.getX() - 1);
                }
                if (avion.getY() < avion.getTargetY()) {
                    avion.setY(avion.getY() + 1);
                } else if (avion.getY() > avion.getTargetY()) {
                    avion.setY(avion.getY() - 1);
                }
                if (avion.getX() == avion.getTargetX() && avion.getY() == avion.getTargetY()) {
                    //moving = false;
                    avion.setMoving(false);
                }
                //this.panel.setLocation(x - offset_x, y - offset_y);
            }
            if (!avion.getMoving()) {
                switch (avion.getEstado()) {
                    case Aterrizar:
                        try {
                            aterrizar();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    case Esperando:
                        avion.getAeropuerto().pedirPermisoAterrizar(avion);
                        //aterrizar el avion y cambiar estado a aterrizando
                        //this.siguientePosicion = posiciones.esperar.get(posiciones.posicionEsperar);
                        avion.setSiguientePosicion(avion.posiciones.esperar.get(avion.posiciones.posicionEsperar));
                        avion.posiciones.posicionEsperar = (avion.posiciones.posicionEsperar + 1) % avion.posiciones.esperar.size();

                        if ((avion.getX() == avion.posiciones.esperar.get(2).x && avion.getY() == avion.posiciones.esperar.get(2).y) && avion.getTienePermisoUsarPista()) {
                            //cambiar de estado a aterrizando por la pista activa del momento
                            //pero llamar a los semaforos y eso en la otra maquina de estados
                            posicion = -1;
                            continuar = true;

                            if (avion.getNumeroPistaUsada() == "01") {
                                //ball.estado = Estados.Aterrizando01;
                                avion.setEstado(Avion.Estados.Aterrizando01);
                            } else if (avion.getNumeroPistaUsada() == "06") {
                                //ball.estado = Estados.Aterrizando06;
                                avion.setEstado(Avion.Estados.Aterrizando06);
                            } else if (avion.getNumeroPistaUsada() == "19") {
                                //ball.estado = Estados.Aterrizando19;
                                avion.setEstado(Avion.Estados.Aterrizando19);
                            } else {
                                //ball.estado = Estados.Aterrizando24;
                                avion.setEstado(Avion.Estados.Aterrizando24);
                            }
                            break;
                        }

                        break;
                    case Aterrizando01:
                        avion.posiciones.posicionEsperar = 0;

                        if (avion.getX() == avion.posiciones.aterrizar01.get(avion.posiciones.aterrizar01.size() - 1).x && avion.getY() == avion.posiciones.aterrizar01.get(avion.posiciones.aterrizar01.size() - 1).y) {
                            //ya salio de la pista, lo cambio de estado a taxeando de 01 a porton
                            //ball.taxear01Porton();
                            /*
                            synchronized (ball.recorrer) {
                                try {
                                    ball.recorrer.notify();
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                            }*/
                            continuar = true;
                            //ball.estado = Estados.Taxeando01Porton;
                            avion.setEstado(Avion.Estados.Taxeando01Porton);
                            posicion = -1;
                            break;
                        }
                        //ball.siguientePosicion = posiciones.aterrizar01.get(posicion);
                        avion.setSiguientePosicion(avion.posiciones.aterrizar01.get(posicion));
                        break;
                    case Aterrizando06:
                        avion.posiciones.posicionEsperar = 0;

                        if (avion.getX() == avion.posiciones.aterrizar06.get(avion.posiciones.aterrizar06.size() - 1).x && avion.getY() == avion.posiciones.aterrizar06.get(avion.posiciones.aterrizar06.size() - 1).y) {
                            //ya salio de la pista, lo cambio de estado a taxeando de 06 a porton
                            //ball.taxear06Porton();
                            /*
                            synchronized (ball.recorrer) {
                                try {
                                    ball.recorrer.notify();
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                            }*/
                            continuar = true;
                            //ball.estado = Estados.Taxeando06Porton;
                            avion.setEstado(Avion.Estados.Taxeando06Porton);
                            posicion = -1;
                            break;
                        }
                        avion.setSiguientePosicion(avion.posiciones.aterrizar06.get(posicion));
                        break;
                    case Aterrizando19:
                        avion.posiciones.posicionEsperar = 0;
                        if (avion.getX() == avion.posiciones.aterrizar19.get(avion.posiciones.aterrizar19.size() - 1).x && avion.getY() == avion.posiciones.aterrizar19.get(avion.posiciones.aterrizar19.size() - 1).y) {
                            //ya salio de la pista, lo cambio de estado a taxeando de 19 a porton
                            //ball.taxear19Porton();

                            continuar = true;
                            //ball.estado = Estados.Taxeando19Porton;
                            avion.setEstado(Avion.Estados.Taxeando19Porton);
                            posicion = -1;
/*
                        synchronized (ball.recorrer) {
                            try {
                                ball.recorrer.notify();
                                //ball.recorrer.lock.unlock();
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }*/
                            break;
                        }
                        avion.setSiguientePosicion(avion.posiciones.aterrizar19.get(posicion));
                        break;
                    case Aterrizando24:
                        avion.posiciones.posicionEsperar = 0;

                        if (avion.getX() == avion.posiciones.aterrizar24.get(avion.posiciones.aterrizar24.size() - 1).x && avion.getY() == avion.posiciones.aterrizar24.get(avion.posiciones.aterrizar24.size() - 1).y) {
                            //ya salio de la pista, lo cambio de estado a taxeando de 24 a porton
                            //ball.taxear24Porton();
/*
                            synchronized (ball.recorrer) {
                                try {
                                    ball.recorrer.notify();
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                            }*/
                            continuar = true;
                            //ball.estado = Estados.Taxeando24Porton;
                            avion.setEstado(Avion.Estados.Taxeando24Porton);
                            posicion = -1;
                            break;
                        }
                        avion.setSiguientePosicion(avion.posiciones.aterrizar24.get(posicion));
                        break;
                    case Despegando01:

                        if (avion.getX() == avion.posiciones.despegar01.get(avion.posiciones.despegar01.size() - 1).x && avion.getY() == avion.posiciones.despegar01.get(avion.posiciones.despegar01.size() - 1).y) {
                            //ya salio de la pista, lo cambio de estado a esperando
                            //ball.estado = Estados.Esperando;
                            avion.getAeropuerto().permisoUsarPista.release();
                            System.out.println(avion.nombre + " despegó y devolvio el uso de la pista 01.");
                            avion.reiniciar();
                            avion.setEstado(Avion.Estados.Esperando);
                            posicion = -1;
                            break;
                        }
                        avion.setSiguientePosicion(avion.posiciones.despegar01.get(posicion));
                        break;
                    case Despegando06:

                        if (avion.getX() == avion.posiciones.despegar06.get(avion.posiciones.despegar06.size() - 1).x && avion.getY() == avion.posiciones.despegar06.get(avion.posiciones.despegar06.size() - 1).y) {
                            //ya salio de la pista, lo cambio de estado a esperando
                            //ball.estado = Estados.Esperando;
                            avion.getAeropuerto().permisoUsarPista.release();
                            System.out.println(avion.nombre + " despegó y devolvio el uso de la pista 06.");
                            avion.reiniciar();
                            avion.setEstado(Avion.Estados.Esperando);
                            posicion = -1;
                            break;
                        }
                        avion.setSiguientePosicion(avion.posiciones.despegar06.get(posicion));
                        break;
                    case Despegando19:
                        if (avion.getX() == avion.posiciones.despegar19.get(avion.posiciones.despegar19.size() - 1).x && avion.getY() == avion.posiciones.despegar19.get(avion.posiciones.despegar19.size() - 1).y) {
                            //ya salio de la pista, lo cambio de estado a esperando
                            avion.getAeropuerto().permisoUsarPista.release();
                            System.out.println(avion.nombre + " despegó y devolvio el uso de la pista 19.");
                            avion.reiniciar();
                            avion.setEstado(Avion.Estados.Esperando);
                            posicion = -1;
                            break;
                        }
                        avion.setSiguientePosicion(avion.posiciones.despegar19.get(posicion));
                        break;
                    case Despegando24:

                        if (avion.getX() == avion.posiciones.despegar24.get(avion.posiciones.despegar24.size() - 1).x && avion.getY() == avion.posiciones.despegar24.get(avion.posiciones.despegar24.size() - 1).y) {
                            //ya salio de la pista, lo cambio de estado a esperando
                            //ball.estado = Estados.Esperando;
                            avion.getAeropuerto().permisoUsarPista.release();
                            System.out.println(avion.nombre + " despegó y devolvio el uso de la pista 24.");
                            avion.reiniciar();
                            avion.setEstado(Avion.Estados.Esperando);
                            posicion = -1;
                            break;
                        }
                        avion.setSiguientePosicion(avion.posiciones.despegar24.get(posicion));
                        break;
                    case Taxeando01Porton:
                        if (avion.getTienePermisoUsarPista()) {
                            //devolver uso de la pista y de recorrer
                            avion.getAeropuerto().permisoUsarPista.release();
                            System.out.println(avion.nombre + " aterrizó y devolvio el uso de la pista 01.");
                            avion.setTienePermisoUsarPista(false);
                            avion.reiniciar();
                            this.recorrer.release();
                        }
                        if (avion.getX() == avion.posiciones.taxear01porton.get(avion.posiciones.taxear01porton.size() - 1).x && avion.getY() == avion.posiciones.taxear01porton.get(avion.posiciones.taxear01porton.size() - 1).y) {
                            //ya llego al porton, lo cambio de estado a en porton
                            //ball.estado = Estados.EnPorton;
                            avion.setEstado(Avion.Estados.EnPorton);
                            posicion = -1;
                            break;
                        }
                        avion.setSiguientePosicion(avion.posiciones.taxear01porton.get(posicion));
                        break;
                    case Taxeando06Porton:
                        if (avion.getTienePermisoUsarPista()) {
                            //devolver uso de la pista y de recorrer
                            avion.getAeropuerto().permisoUsarPista.release();
                            System.out.println(avion.nombre + " aterrizó y devolvio el uso de la pista 06.");
                            avion.setTienePermisoUsarPista(false);
                            avion.reiniciar();
                            this.recorrer.release();
                        }
                        if (avion.getX() == avion.posiciones.taxear06porton.get(avion.posiciones.taxear06porton.size() - 1).x && avion.getY() == avion.posiciones.taxear06porton.get(avion.posiciones.taxear06porton.size() - 1).y) {
                            //ya llego al porton, lo cambio de estado a en porton
                            //ball.estado = Estados.EnPorton;
                            avion.setEstado(Avion.Estados.EnPorton);
                            posicion = -1;
                            break;
                        }
                        avion.setSiguientePosicion(avion.posiciones.taxear06porton.get(posicion));
                        break;
                    case Taxeando19Porton:
                        if (avion.getTienePermisoUsarPista()) {
                            //devolver uso de la pista y de recorrer
                            avion.getAeropuerto().permisoUsarPista.release();
                            System.out.println(avion.nombre + " aterrizó y devolvio el uso de la pista 19.");
                            avion.setTienePermisoUsarPista(false);
                            avion.reiniciar();
                            this.recorrer.release();
                        }
                        if (avion.getX() == avion.posiciones.taxear19porton.get(avion.posiciones.taxear19porton.size() - 1).x && avion.getY() == avion.posiciones.taxear19porton.get(avion.posiciones.taxear19porton.size() - 1).y) {
                            //ya llego al porton, lo cambio de estado a en porton
                            continuar = true;
                            //ball.estado = Estados.EnPorton;
                            avion.setEstado(Avion.Estados.EnPorton);
                            posicion = -1;
                            break;
                        }
                        avion.setSiguientePosicion(avion.posiciones.taxear19porton.get(posicion));
                        break;
                    case Taxeando24Porton:
                        if (avion.getTienePermisoUsarPista()) {
                            //devolver uso de la pista y de recorrer
                            avion.getAeropuerto().permisoUsarPista.release();
                            System.out.println(avion.nombre + " aterrizó y devolvio el uso de la pista 24.");
                            avion.setTienePermisoUsarPista(false);
                            avion.reiniciar();
                            this.recorrer.release();
                        }
                        if (avion.getX() == avion.posiciones.taxear24porton.get(avion.posiciones.taxear24porton.size() - 1).x && avion.getY() == avion.posiciones.taxear24porton.get(avion.posiciones.taxear24porton.size() - 1).y) {
                            //ya llego al porton, lo cambio de estado a en porton
                            //ball.estado = Estados.EnPorton;
                            avion.setEstado(Avion.Estados.EnPorton);
                            posicion = -1;
                            break;
                        }
                        avion.setSiguientePosicion(avion.posiciones.taxear24porton.get(posicion));
                        break;
                    case TaxeandoPorton01:

                        if (avion.getX() == avion.posiciones.taxearporton01.get(avion.posiciones.taxearporton01.size() - 1).x && avion.getY() == avion.posiciones.taxearporton01.get(avion.posiciones.taxearporton01.size() - 1).y) {
                            //ya llego a la pista, lo cambio a despegando por la pista 01
                            //ball.estado = Estados.Despegando01;
                            try {
                                pedirPermisoParaUsarPista();
                                System.out.println(avion.nombre + " va a usar la pista 01 para despegar.");
                            } catch (InterruptedException e) {
                                System.out.println("Excepcion en taxeandoPorton01");
                                throw new RuntimeException(e);
                            }
                            avion.setEstado(Avion.Estados.Despegando01);
                            posicion = -1;
                            break;
                        }
                        avion.setSiguientePosicion(avion.posiciones.taxearporton01.get(posicion));
                        break;
                    case TaxeandoPorton06:

                        if (avion.getX() == avion.posiciones.taxearporton06.get(avion.posiciones.taxearporton06.size() - 1).x && avion.getY() == avion.posiciones.taxearporton06.get(avion.posiciones.taxearporton06.size() - 1).y) {
                            //ya llego a la pista, lo cambio a despegando por la pista 06
                            //ball.estado = Estados.Despegando06;
                            try {
                                pedirPermisoParaUsarPista();
                                System.out.println(avion.nombre + " va a usar la pista 06 para despegar.");
                            } catch (InterruptedException e) {
                                System.out.println("Excepcion en taxeandoPorton06");
                                throw new RuntimeException(e);
                            }
                            avion.setEstado(Avion.Estados.Despegando06);
                            posicion = -1;
                            break;
                        }
                        avion.setSiguientePosicion(avion.posiciones.taxearporton06.get(posicion));
                        break;
                    case TaxeandoPorton19:

                        if (avion.getX() == avion.posiciones.taxearporton19.get(avion.posiciones.taxearporton19.size() - 1).x && avion.getY() == avion.posiciones.taxearporton19.get(avion.posiciones.taxearporton19.size() - 1).y) {
                            //ya llego a la pista, lo cambio a despegando por la pista 19
                            continuar = true;
                            //ball.estado = Estados.Despegando19;
                            try {
                                pedirPermisoParaUsarPista();
                                System.out.println(avion.nombre + " va a usar la pista 19 para despegar.");
                            } catch (InterruptedException e) {
                                System.out.println("Excepcion en taxeandoPorton19");
                                throw new RuntimeException(e);
                            }
                            avion.setEstado(Avion.Estados.Despegando19);
                            posicion = -1;
                            break;
                        }
                        avion.setSiguientePosicion(avion.posiciones.taxearporton19.get(posicion));
                        break;
                    case TaxeandoPorton24:

                        if (avion.getX() == avion.posiciones.taxearporton24.get(avion.posiciones.taxearporton24.size() - 1).x && avion.getY() == avion.posiciones.taxearporton24.get(avion.posiciones.taxearporton24.size() - 1).y) {
                            //ya llego a la pista, lo cambio a despegando por la pista 24
//                            ball.estado = Estados.Despegando24;
                            try {
                                pedirPermisoParaUsarPista();
                                System.out.println(avion.nombre + " va a usar la pista 24 para despegar.");
                            } catch (InterruptedException e) {
                                System.out.println("Excepcion en taxeandoPorton24");
                                throw new RuntimeException(e);
                            }
                            avion.setEstado(Avion.Estados.Despegando24);
                            posicion = -1;
                            break;
                        }
                        avion.setSiguientePosicion(avion.posiciones.taxearporton24.get(posicion));
                        break;
                    case EnPorton:
                        //esperar algunos segundos random, y despues ver cual es la pista activa y taxear a ella
                        Random random = new Random();
                        try {
                            Thread.sleep(random.nextLong(20000));
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        taxearPistaActiva();
                        break;
                }
                moveTo(avion.getSiguientePosicion());
                avion.getPanel().cambiarEstado(avion.getEstado().toString());
                posicion++;
            }

            /*
            if (ball.getX() == ball.getSiguientePosicion().x && ball.getY() == ball.getSiguientePosicion().y) {
                ball.moving = false;
            }*/

            try {
                //System.out.println("Thread actual: " + Thread.currentThread().getName());
                Thread.sleep(WAIT);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }

        }

    }

    public void moveTo(Posicion pos) {
        int targetX = pos.x;
        int targetY = pos.y;
        double angulo = calcRotationAngleInDegrees(new Posicion(avion.getX(), avion.getY(), false), new Posicion(targetX, targetY, false));
        //this.offset_x = (int) (radio * Math.cos(angulo));
        //this.offset_y = (int) (radio * Math.sin(angulo));
        //this.panel.rotarIcono(angulo);
        avion.getPanel().rotarIcono(angulo);
        avion.setTargetX(targetX);
        //this.targetX = targetX;
        avion.setTargetY(targetY);
        //this.targetY = targetY;
        avion.setMoving(true);
    }

    private double calcRotationAngleInDegrees(Posicion centerPt, Posicion targetPt) {
        // calculate the angle theta from the deltaY and deltaX values
        // (atan2 returns radians values from [-PI,PI])
        // 0 currently points EAST.
        // NOTE: By preserving Y and X param order to atan2,  we are expecting
        // a CLOCKWISE angle direction.
        double theta = Math.atan2(targetPt.y - centerPt.y, targetPt.x - centerPt.x);

        // rotate the theta angle clockwise by 90 degrees
        // (this makes 0 point NORTH)
        // NOTE: adding to an angle rotates it clockwise.
        // subtracting would rotate it counter-clockwise
        theta += Math.PI / 2.0;

        // convert from radians to degrees
        // this will give you an angle from [0->270],[-180,0]
        double angle = Math.toDegrees(theta);

        // convert to positive range [0-360)
        // since we want to prevent negative angles, adjust them now.
        // we can assume that atan2 will not return a negative value
        // greater than one partial rotation
        if (angle < 0) {
            angle += 360;
        }

        return angle;
    }

    public void aterrizar() throws InterruptedException {
        avion.setTienePermisoUsarPista(false);
        //pedir permiso para usar la pista, si no tiene prioridad, y despues usar la pista
        /*
        this.pidioPermisoParaAterrizar = false;
        this.tienePermisoUsarPista = false;
        System.out.println("ATERRIZAR: " + this.nombre);

        if (this.prioridad != 0) {
            pidioPermisoParaAterrizar = true;
            aeropuerto.permisoUsarPista.acquire();
            System.out.println(this.nombre + " pidio permiso para aterrizar");
        }
*/
        Object[] pista = avion.getAeropuerto().getPistaActiva();
        //avion.pistaUsada = (Pista) pista[0];
        avion.setPistaUsada((Pista) pista[0]);

        //boolean res = avion.getPistaUsada().usar.tryAcquire();
        boolean res = avion.getAeropuerto().permisoUsarPista.tryAcquire();
        if (!res) {
            //pista ocupada
            avion.setEstado(Avion.Estados.Esperando);
        } else {
            //pista disponible
            avion.setTienePermisoUsarPista(true);
            avion.setNumeroPistaUsada(pista[1].toString());
            posicion = 0;
            if (pista[1].toString() == "01") {
                avion.setEstado(Avion.Estados.Aterrizando01);
                //this.aterrizar01();

            } else if (pista[1].toString() == "19") {
                avion.setEstado(Avion.Estados.Aterrizando19);

            } else if (pista[1].toString() == "06") {
                avion.setEstado(Avion.Estados.Aterrizando06);
                //this.aterrizar06();
            } else {
                //pista 24
                avion.setEstado(Avion.Estados.Aterrizando24);
                //this.aterrizar24();
            }
            System.out.println(avion.nombre + " va a usar la pista " + pista[1].toString() + " para aterrizar.");
            //actualiza el estado en la parte grafica
        }
    }

    private void pedirPermisoParaUsarPista() throws InterruptedException {
        /*
        while (aeropuerto.getPistaOcupada()) {
            Thread t = aeropuerto.threads.get(this.nombre);
            System.out.println("Thread " + t.getName());
            synchronized (t) {
                t.wait();
            }
        }
        aeropuerto.setPistaOcupada(true);
    */
        //aeropuerto.pistaActiva.usar.acquire();
        avion.getAeropuerto().permisoUsarPista.acquire();
        avion.setTienePermisoUsarPista(true);
    }

    public void taxearPistaActiva() {
        String pistaActiva = avion.getAeropuerto().getNumeroPistaActiva();
        if (pistaActiva == "01") {
            posicion = 0;
            avion.setEstado(Avion.Estados.TaxeandoPorton01);
        } else if (pistaActiva == "06") {
            posicion = 0;
            avion.setEstado(Avion.Estados.TaxeandoPorton06);
        } else if (pistaActiva == "19") {
            posicion = 0;
            avion.setEstado(Avion.Estados.TaxeandoPorton19);
        } else {
            //pista 24
            posicion = 0;
            avion.setEstado(Avion.Estados.TaxeandoPorton24);
        }
    }

}
