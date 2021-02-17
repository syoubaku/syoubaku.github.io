package info.xiaomo.gengine.network.netty.code;

import info.xiaomo.gengine.network.netty.handler.DefaultClientInBoundHandler;
import info.xiaomo.gengine.network.netty.handler.DefaultOutBoundHandler;
import info.xiaomo.gengine.network.netty.service.NettyClientService;
import info.xiaomo.gengine.network.server.ServerInfo;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * 客户端默认初始化channel
 * ，服务器内部使用客户端
 * <p>
 * <p>
 * 2017年8月25日 上午9:28:47
 */
public class DefaultClientChannelInitializer extends ChannelInitializer<SocketChannel> {
	protected final NettyClientService nettyClientService;
	protected ServerInfo serverInfo;


	public DefaultClientChannelInitializer(NettyClientService nettyClientService, ServerInfo serverInfo) {
		this.nettyClientService = nettyClientService;
		this.serverInfo = serverInfo;
	}

	public DefaultClientChannelInitializer(NettyClientService nettyClientService) {
		this.nettyClientService = nettyClientService;
	}

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ch.pipeline().addLast(new DefaultOutBoundHandler());
		ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(50 * 1024, 0, 4));    // 消息包格式:长度(4)+角色ID(8)+消息ID(4)+内容
		ch.pipeline().addLast(new DefaultMessageCodec(4)); //消息加解密
		ch.pipeline().addLast(new DefaultClientInBoundHandler(nettyClientService, serverInfo)); //消息处理器
	}

}
