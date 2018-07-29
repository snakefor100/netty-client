package com.junlong.netty.demo.nettyclient.test1;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * @author niuniu
 * @version 1.0.0
 * @date 2018/7/29
 * @since 1.0.0
 */
public class EchoClient {
    private final String url;
    private final int port;

    public EchoClient(String url, int port) {
        this.url = url;
        this.port = port;
    }

    public static void main(String[] args) throws InterruptedException {

        new EchoClient("localhost",8888).start();
    }

    public void start() throws InterruptedException {
        NioEventLoopGroup eventExecutors = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventExecutors).channel(NioSocketChannel.class).remoteAddress(new InetSocketAddress(url,port))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new EchoClienthandler());
                        }
                    });
            ChannelFuture f = bootstrap.connect().sync();
            f.channel().closeFuture().sync();
        }finally {
            eventExecutors.shutdownGracefully().sync();
        }

    }
}
