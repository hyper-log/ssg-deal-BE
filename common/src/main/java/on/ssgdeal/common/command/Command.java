package on.ssgdeal.common.command;

public interface Command<T> {

    T execute();

    void undo();
}