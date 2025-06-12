package org.example;

public class AddMasterModelCommand implements Command {
    private final MasterModelDao dao;
    private final MasterModel itemToAdd;

    public AddMasterModelCommand(MasterModelDao dao, MasterModel itemToAdd) {
        this.dao = dao;
        this.itemToAdd = itemToAdd;
    }

    @Override
    public void execute() {
        dao.save(itemToAdd);
        System.out.println("[COMMAND] AddMasterModelCommand dieksekusi untuk ID: " + itemToAdd.getId());
    }
}