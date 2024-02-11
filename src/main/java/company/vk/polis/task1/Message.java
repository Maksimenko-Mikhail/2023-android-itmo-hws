package company.vk.polis.task1;

import org.jetbrains.annotations.Nullable;

record Message(Integer id, String text, Integer senderId, Long timestamp, State state) implements Entity {
    @Override
    public Integer getId() {
        return id;
    }
}
