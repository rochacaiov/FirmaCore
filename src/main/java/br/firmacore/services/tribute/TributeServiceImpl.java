package br.firmacore.services.tribute;

import br.firmacore.Main;
import br.firmacore.services.tribute.cache.TributeManager;
import br.firmacore.services.tribute.exceptions.TributeNotExistsException;
import br.firmacore.services.tribute.repository.TributeRepository;
import br.firmacore.services.tribute.repository.models.Tribute;
import br.firmacore.enums.PropertyType;
import br.firmacore.hooks.VaultHook;
import br.firmacore.hooks.exceptions.PlayerHasNoMoneyException;
import br.firmacore.services.tribute.api.TributeService;
import br.firmacore.services.tribute.exceptions.TributeTypeException;
import br.firmacore.utils.MessageUtils;
import br.firmacore.utils.TributeUtils;
import org.bukkit.entity.Player;

public class TributeServiceImpl implements TributeService {
    public static final String TRIBUTE_TAG = " &c*&c&lTRIBUTO&c* &8>> ";
    public static final double HOUSE_PERCENTAGE = 0.3;
    public static final double STORE_PERCENTAGE = 0.5;
    public static final int LIMIT_TRIBUTE = 6;
    private TributeManager tributeManager;
    private TributeRepository tributeRepository;

    public TributeServiceImpl(Main plugin){
        //this.tributeManager = tributeController.getTributeManager();
        //this.tributeRepository = plugin.get.getTributeRepository();

        //this.tributeRepository.getAll().forEach(house -> {this.tributeManager.add(house);});

        //new TributeRunnable(this.tributeManager).runTaskTimer(plugin, 0, 20 * 60);
    }

    @Override
    public void createTribute(String uuid, int size, PropertyType propertyType) {

    }

    /**@Override
    public void createTribute(String uuid, int size, PropertyType propertyType){
        Tribute tribute = null;
        if(propertyType.equals(PropertyType.CASA)){
            tribute = new Tribute(
                    uuid,
                    propertyType,
                    calculateValue(size, propertyType)
            );
        }
        if(propertyType.equals(PropertyType.LOJA)){
            tribute = new Tribute(
                    uuid,
                    propertyType,
                    calculateValue(size, propertyType)
            );
        }
        this.tributeManager.add(tribute);
        this.tributeRepository.saveOrUpdate(tribute);
    }**/

    @Override
    public double payTribute(Player owner, String propertyType) throws TributeTypeException,
            TributeNotExistsException, PlayerHasNoMoneyException {

        Tribute tribute = this.tributeManager.getTributeType(
                owner.getName(),
                TributeUtils.stringToPropertyType(propertyType)
        );

        double tributeValue = tribute.getValue();

        if (!VaultHook.playerHasMoney(owner, tributeValue)) throw new PlayerHasNoMoneyException();

        tribute.setValue(0);
        tribute.setAmount(0);
        this.tributeManager.saveOrUpdate(tribute);

        return tributeValue;
    }

    @Override
    public void removeTribute(String owner, PropertyType propertyType) throws TributeNotExistsException {

    }

    public void listTributes(Player owner) throws TributeNotExistsException {
        this.tributeManager.getTributesOwner(owner.getName()).forEach(tribute -> {
            MessageUtils.messageToPlayer(owner, "Lista de Tributos");
            if(tribute.getPropertyType() == PropertyType.CASA){
                MessageUtils.messageToPlayer(owner, "CATEGORIA CASA");
                MessageUtils.messageToPlayer(owner, "VALOR: " + tribute.getValue());
                MessageUtils.messageToPlayer(owner, "QTD: " + tribute.getAmount());
            }
            if(tribute.getPropertyType() == PropertyType.LOJA){
                MessageUtils.messageToPlayer(owner, "CATEGORIA CASA");
                MessageUtils.messageToPlayer(owner, "VALOR: " + tribute.getValue());
                MessageUtils.messageToPlayer(owner, "QTD: " + tribute.getAmount());
            }

        });
    }

    /**@Override
    public void removeTribute(Tribute tribute) {
        this.tributeManager.remove(tribute);
        this.tributeRepository.delete(tribute);
    }**/

    public Tribute getTributeType(String owner, PropertyType propertyType) throws TributeNotExistsException {
        return this.tributeManager.getTributeType(owner, propertyType);
    }

    /**public static double calculateValue(int size, PropertyType propertyType){
        if(propertyType.equals(PropertyType.CASA)){
            return TributeController.HOUSE_PERCENTAGE * size;
        }
        if(propertyType.equals(PropertyType.LOJA)){
            return TributeController.STORE_PERCENTAGE * size;
        }
        return 0;
    }**/
}
