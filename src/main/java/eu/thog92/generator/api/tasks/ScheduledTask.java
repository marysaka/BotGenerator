package eu.thog92.generator.api.tasks;

import eu.thog92.generator.api.BotGenerator;

public abstract class ScheduledTask implements ITask<Boolean>, Runnable
{

    protected final BotGenerator botGenerator;
    protected boolean isCancelled;
    protected int delay;
    protected ITaskManager manager;

    public ScheduledTask()
    {
        this.botGenerator = BotGenerator.getInstance();
        this.manager = botGenerator.getTasksManager();
    }

    @Override
    public void run()
    {
        try
        {
            if (!this.execute())
            {
                this.cancel();
            }
            this.manager.onFinishTask(this);
        } catch (Exception ex)
        {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    public boolean isCancelled()
    {
        return isCancelled;
    }

    protected void cancel()
    {
        this.isCancelled = true;
    }

    public int getDelay()
    {
        return delay;
    }

    public void setDelay(int delay)
    {
        this.delay = delay;
    }

    public String getName()
    {
        return this.getClass().getSimpleName();
    }
}
