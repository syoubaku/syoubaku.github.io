package info.xiaomo.gengine.thread.timer;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 独立周期性单线程
 * <p>
 * <p>
 * 2017年7月3日 上午10:43:19
 */
public abstract class ScheduledTask {
	private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledTask.class);
	private final ScheduledExecutorService scheduler;
	private final int period; // 周期

	/**
	 * @param taskMaxTime 执行周期
	 */
	public ScheduledTask(int taskMaxTime) {
		scheduler = Executors.newScheduledThreadPool(1);

		Executors.newScheduledThreadPool(1, new ThreadFactory() {
			@Override
			public Thread newThread(Runnable r) {
				return new Thread(getClass().getSimpleName());
			}
		});
		period = Math.max(taskMaxTime, 100);
	}

	/**
	 * 任务逻辑
	 */
	protected abstract void executeTask();

	/**
	 * 启动线程
	 */
	public void start() {
		scheduler.scheduleAtFixedRate(new Task(), 100, period, TimeUnit.MILLISECONDS);
	}

	/**
	 * 关闭调用
	 */
	public void shutdown() {
		scheduler.shutdown();
	}

	/**
	 * 任务
	 * <p>
	 * <p>
	 * 2017年7月3日 上午10:45:01
	 */
	class Task implements Runnable {

		@Override
		public void run() {
			try {
				long begin = System.currentTimeMillis();
				executeTask();
				if (System.currentTimeMillis() - begin > period) {
					LOGGER.warn("定时器{}执行{}ms", getClass().getSimpleName(), System.currentTimeMillis() - begin);
				}
			} catch (Exception e) {
				LOGGER.error("定时任务", e);
			}
		}
	}
}
