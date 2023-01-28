import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Main {
    public static void main(String[] args) {
        GameProgress game1 = new GameProgress(100, 1, 1, 2);
        GameProgress game2 = new GameProgress(85, 3, 2, 4.5);
        GameProgress game3 = new GameProgress(65, 4, 5, 6.5);

        saveGame("C://Games/savegames/save1.dat", game1);
        saveGame("C://Games/savegames/save2.dat", game2);
        saveGame("C://Games/savegames/save3.dat", game3);

        zipFiles("C://Games/savegames/game.zip", "C://Games/savegames");

        deleteFiles("C://Games/savegames");

        openZip("C://Games/savegames/game.zip", "C://Games/savegames");

        System.out.println(openProgress("C://Games/savegames/save3.dat").toString());
    }

    public static void saveGame(String way, GameProgress game) {
        try (FileOutputStream fos = new FileOutputStream(way);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(game);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void zipFiles(String zipWay, String objWay) {
        try (ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(zipWay))) {
            File dir = new File(objWay);
            if (dir.isDirectory()) {
                for (File file : dir.listFiles()) {
                    if (file.isFile() && file.getName().indexOf(".dat") != -1) {
                        FileInputStream fis = new FileInputStream(file);
                        ZipEntry entry = new ZipEntry(file.getName());
                        zout.putNextEntry(entry);
                        byte[] buffer = new byte[fis.available()];
                        fis.read(buffer);
                        zout.write(buffer);
                        fis.close();
                    }
                }
            }
            zout.closeEntry();
        } catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    public static void deleteFiles(String objWay){
        File dir = new File(objWay);
        try {
            if (dir.isDirectory()) {
                for (File file : dir.listFiles()) {
                    if (file.isFile() && file.getName().indexOf(".dat") != -1) {
                        File newDir = new File(file.getParent().replace('\\', '/') + "/" + file.getName());
                        newDir.delete();
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }

    public static void openZip(String fileWay, String openZipWay){
        try (ZipInputStream zin = new ZipInputStream
                (new FileInputStream(fileWay))){
            ZipEntry entry;
            String name;
            while ((entry = zin.getNextEntry()) != null){
                name = entry.getName();
                FileOutputStream fout = new FileOutputStream(openZipWay + "/" + name);
                for (int c = zin.read(); c != -1; c = zin.read()){
                    fout.write(c);
                }
                fout.flush();
                zin.closeEntry();
                fout.close();
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static GameProgress openProgress(String fileWay) {
        GameProgress gameProgress = null;
        try (FileInputStream fis = new FileInputStream(fileWay);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            gameProgress = (GameProgress) ois.readObject();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return gameProgress;
    }
}