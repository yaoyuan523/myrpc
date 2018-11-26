package client;

import common.bean.RpcRequest;
import common.bean.RpcResponse;
import common.codec.RpcDecoder;
import common.codec.RpcEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * RPC 客户端（用于发送 RPC 请求）
 * Created by loovee on 2018/3/14.
 */
public class RpcClient extends SimpleChannelInboundHandler<RpcResponse> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RpcClient.class);

    private final String host;
    private final int port;

    private RpcResponse response;

    public RpcClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

        LOGGER.error("api caught exception", cause);
        try {
            ctx.close();
        } catch (Throwable e) {
            LOGGER.error("close channel ", e.getMessage());
        }
    }

    public RpcResponse send(RpcRequest request) throws Exception {
        EventLoopGroup group = null;
        try {
            group = new NioEventLoopGroup();
            // 创建并初始化 Netty 客户端 Bootstrap 对象
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel channel) throws Exception {
                    ChannelPipeline pipeline = channel.pipeline();
                    pipeline.addLast(new RpcEncoder(RpcRequest.class)); // 编码
                    // RPC
                    // 请求
                    pipeline.addLast(new RpcDecoder(RpcResponse.class)); // 解码
                    // RPC
                    // 响应
                    pipeline.addLast(RpcClient.this); // 处理 RPC 响应
                }
            });

            bootstrap.option(ChannelOption.TCP_NODELAY, true);
            // 连接 RPC 服务器
            ChannelFuture future = bootstrap.connect(host, port).sync();
            // 写入 RPC 请求数据并关闭连接
            Channel channel = future.channel();
            channel.writeAndFlush(request).sync();
            channel.closeFuture().sync();
            // 返回 RPC 响应对象
            return response;
        } finally {
            group.shutdownGracefully();
        }
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, RpcResponse response) throws Exception {
        // TODO Auto-generated method stub
        this.response = response;
    }

   /* @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcResponse rpcResponse) throws Exception {
        this.response = response;
    }*/
}