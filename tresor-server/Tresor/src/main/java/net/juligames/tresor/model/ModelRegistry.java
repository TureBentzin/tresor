package net.juligames.tresor.model;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class ModelRegistry {

    private @NotNull HashMap<Class<?>, Object> models = new HashMap<>();

    public ModelRegistry() {

    }

    public <T> @NotNull T getModel(@NotNull Class<T> modelClass) {
        try {
            return modelClass.cast(models.get(modelClass));
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("Model class " + modelClass.getName() + " is not registered");
        }
    }

    protected  <T> void registerModel(@NotNull Class<T> modelClass, @NotNull T model) {
        models.put(modelClass, model);
    }

    public void unregisterModel(@NotNull Class<?> modelClass) {
        models.remove(modelClass);
    }

}
