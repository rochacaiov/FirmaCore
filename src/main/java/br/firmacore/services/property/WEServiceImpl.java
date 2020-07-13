package br.firmacore.services.property;

import br.firmacore.services.property.vo.WEBorderVO;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

public class WEServiceImpl {

    public static void createBorder(WEBorderVO weBorderVO, Material materialType) {
        World world = weBorderVO.getWorld();
        int wallXMax = weBorderVO.getWallXMax();
        int wallZMax = weBorderVO.getWallZMax();
        int wallXMin = weBorderVO.getWallXMin();
        int wallZMin = weBorderVO.getWallZMin();

        for (int x = wallXMin; x <= wallXMax; x++) {
            world.getBlockAt(x, (world.getHighestBlockYAt(x, wallZMax) + 1), wallZMax).setType(materialType);
            world.getBlockAt(x, (world.getHighestBlockYAt(x, wallZMin) + 1), wallZMin).setType(materialType);
        }

        for (int z = wallZMin; z <= wallZMax; z++) {
            world.getBlockAt(wallXMax, (world.getHighestBlockYAt(wallXMax, z) + 1), z).setType(materialType);
            world.getBlockAt(wallXMin, (world.getHighestBlockYAt(wallXMin, z) + 1), z).setType(materialType);
        }
    }

    public static void removeBorder(WEBorderVO weBorderVO, Material materialType) {
        World world = weBorderVO.getWorld();
        int wallXMax = weBorderVO.getWallXMax();
        int wallZMax = weBorderVO.getWallZMax();
        int wallXMin = weBorderVO.getWallXMin();
        int wallZMin = weBorderVO.getWallZMin();

        for (int y = 0; y <= 256; y++) {
            for (int x = wallXMin; x <= wallXMax; x++) {
                for (int z = wallZMin; z <= wallZMax; z++) {
                    Block wallX1 = world.getBlockAt(x, y, wallZMax);
                    Block wallX2 = world.getBlockAt(x, y, wallZMin);
                    Block wallZ1 = world.getBlockAt(wallXMax, y, z);
                    Block wallZ2 = world.getBlockAt(wallXMin, y, z);
                    if (wallX1.getType() == materialType || wallX2.getType() == materialType) {
                        wallX1.setType(Material.AIR);
                        wallX2.setType(Material.AIR);
                    }
                    if (wallZ1.getType() == materialType || wallZ2.getType() == materialType) {
                        wallZ1.setType(Material.AIR);
                        wallZ2.setType(Material.AIR);
                    }
                }

            }
        }
    }

    /**private static void removeTrees(World world, int tamanho, Location loc) {
     for(int y = 20; y < 256; y++){
     for(int x = loc.getBlockX() - (tamanho/2); x < loc.getBlockX() - (tamanho/2) + tamanho; x++){
     for(int z = loc.getBlockZ() - (tamanho/2); z < loc.getBlockZ() - (tamanho/2) +tamanho; z++){
     Block block = new Location(world, x, y, z).getBlock();
     if(block.getType() == Material.OAK_LEAVES || block.getType() == Material.OAK_LOG){
     block.setType(Material.AIR);
     }
     }
     }
     }
     }**/
}
