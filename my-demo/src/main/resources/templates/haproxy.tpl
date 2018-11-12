global
        log 127.0.0.1 local0
        maxconn 20480
        daemon
        user root
        nbproc 1
        pidfile /usr/local/haproxy/run/haproxy.pid

defaults
        log    global
        mode    tcp
        maxconn 50000
        option  httplog
        option  httpclose
        option  dontlognull
        option  forwardfor
        retries 3
        option redispatch
        stats refresh 30
        option abortonclose
        balance roundrobin
        timeout http-request    10s
        timeout queue           1m
        timeout connect         10s
        timeout client          1m
        timeout server          1m
        timeout http-keep-alive 10s
        timeout check           10s

<#list front_and_back as single>
frontend ${single.frontend_name}
    bind 0.0.0.0:${single.listen_port}
    mode ${single.mode}
    log global
    option httplog
    option httpclose
    option forwardfor
    default_backend ${single.backend_name}

backend ${single.backend_name}
    mode ${single.mode}
    option  redispatch
    option  abortonclose
    balance ${single.load_balance_strategy}
    cookie SERVERID
    <#list single.servers as server>
    server ${server.server_name} ${server.ip}:${server.port} check port ${server.port} inter 1000 fall ${(server.max_fails)!'3'}
    </#list>

</#list>