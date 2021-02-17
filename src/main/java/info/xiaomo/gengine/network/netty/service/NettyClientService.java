package info.xiaomo.gengine.network.netty.service;

import java.util.concurrent.PriorityBlockingQueue;
import info.xiaomo.gengine.network.netty.config.NettyClientConfig;
import info.xiaomo.gengine.network.server.GameService;
import info.xiaomo.gengine.network.server.ITcpClientService;
import info.xiaomo.gengine.thread.ThreadPoolExecutorConfig;
import io.netty.channel.Channel;

/**
 * Netty 内部客户端
 * <p>
 * <p>
 * 2017年8月25日 下午2:44:39
 */
public abstract class NettyClientService extends GameService<NettyClientConfig> implements ITcpClientService<NettyClientConfig> {
	private NettyClientConfig nettyClientConfig;

	/**
	 * 拥有的连接
	 */
	private final PriorityBlockingQueue<Channel> channels = new PriorityBlockingQueue<>(64, (c1, c2) -> {
		long res = c1.bytesBeforeUnwritable() - c2.bytesBeforeUnwritable();
		if (res == 0) {
			res = c1.bytesBeforeWritable() - c2.bytesBeforeWritable();
		}
		return (int) res;
	});

	/**
	 * 不开启线程池
	 *
	 * @param nettyClientConfig
	 */
	public NettyClientService(NettyClientConfig nettyClientConfig) {
		this(null, nettyClientConfig);
	}

	public NettyClientService(ThreadPoolExecutorConfig threadPoolExecutorConfig, NettyClientConfig nettyClientConfig) {
		super(threadPoolExecutorConfig);
		this.nettyClientConfig = nettyClientConfig;
	}

	public NettyClientConfig getNettyClientConfig() {
		return nettyClientConfig;
	}

	public void setNettyClientConfig(NettyClientConfig nettyClientConfig) {
		this.nettyClientConfig = nettyClientConfig;
	}

	/**
	 * 连接创建
	 * <p>
	 * <p>
	 * 2017年8月25日 下午3:27:03
	 *
	 * @param channel
	 */
	public void channelActive(Channel channel) {
		channels.add(channel);
	}

	/**
	 * 连接断开
	 * <p>
	 * <p>
	 * 2017年8月25日 下午3:27:19
	 *
	 * @param channel
	 */
	public void channelInactive(Channel channel) {
		channels.remove(channel);
	}

	/**
	 * 获取空闲连接
	 * <p>
	 * <p>
	 * 2017年8月25日 下午3:49:58
	 *
	 * @return
	 */
	public Channel getMostIdleChannel() {
		Channel channel = null;
		while (channel == null && !channels.isEmpty()) {
			channel = channels.peek();
			if (channel != null && channel.isActive()) {
				break;
			} else {
				channels.poll();
			}
		}
		return channel;
	}

	/**
	 * 发送消息
	 *
	 * @param obj
	 * @return
	 */
	public boolean sendMsg(Object obj) {
		Channel channel = getMostIdleChannel();
		if (channel != null) {
			channel.writeAndFlush(obj);
			return true;
		}
		return false;
	}


}
