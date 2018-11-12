daemon off;
user  root;
worker_processes ${worker_processes};
pid /var/run/nginx.pid;

events {
    multi_accept        on;
    worker_connections  ${worker_connections};
    use                 epoll;
}

http {
        real_ip_header      X-Forwarded-For;

        real_ip_recursive   on;

        sendfile            on;
        aio                 threads;
        tcp_nopush          on;
        tcp_nodelay         on;

        log_subrequest      on;

        reset_timedout_connection on;

        keepalive_timeout  300s;
        keepalive_requests 20000;

        client_header_buffer_size       1k;
        large_client_header_buffers     4 8k;
        client_body_buffer_size         8k;

        http2_max_field_size            4k;
        http2_max_header_size           16k;

        types_hash_max_size             2048;
        server_names_hash_max_size      1024;
        server_names_hash_bucket_size   32;
        map_hash_bucket_size            64;

        variables_hash_bucket_size      64;
        variables_hash_max_size         2048;

        underscores_in_headers          off;
        ignore_invalid_headers          on;

        include /usr/local/openresty/nginx/conf/mime.types;
        default_type text/html;
        gzip on;
        gzip_comp_level 5;
        gzip_http_version 1.1;
        gzip_min_length 256;
        gzip_types application/atom+xml application/javascript application/x-javascript application/json application/rss+xml application/vnd.ms-fontobject application/x-font-ttf application/x-web-app-manifest+json application/xhtml+xml application/xml font/opentype image/svg+xml image/x-icon text/css text/plain text/x-component;
        gzip_proxied any;

        server_tokens on;

        # disable warnings
        uninitialized_variable_warn off;

        log_format upstreaminfo '$the_real_ip - [$the_real_ip] - $remote_user [$time_local] "$request" $status $body_bytes_sent "$http_referer" "$http_user_agent" $request_length $request_time [$proxy_upstream_name] $upstream_addr $upstream_response_length $upstream_response_time $upstream_status';

        map $request_uri $loggable {
            default 1;
        }

        access_log /var/log/nginx/access.log upstreaminfo if=$loggable;
        error_log  /var/log/nginx/error.log notice;

        # Retain the default nginx handling of requests without a "Connection" header
        map $http_upgrade $connection_upgrade {
            default          upgrade;
            ''               close;
        }

        # trust http_x_forwarded_proto headers correctly indicate ssl offloading
        map $http_x_forwarded_proto $pass_access_scheme {
            default          $http_x_forwarded_proto;
            ''               $scheme;
        }

        map $http_x_forwarded_port $pass_server_port {
           default           $http_x_forwarded_port;
           ''                $server_port;
        }

        map $http_x_forwarded_for $the_real_ip {
            default          $http_x_forwarded_for;
            ''               $remote_addr;
        }

        # map port 442 to 443 for header X-Forwarded-Port
        map $pass_server_port $pass_port {
            442              443;
            default          $pass_server_port;
        }

        # Map a response error watching the header Content-Type
        map $http_accept $httpAccept {
            default          html;
            application/json json;
            application/xml  xml;
            text/plain       text;
        }

        map $httpAccept $httpReturnType {
            default          text/html;
            json             application/json;
            xml              application/xml;
            text             text/plain;
        }

        # Obtain best http host
        map $http_host $this_host {
            default          $http_host;
            ''               $host;
        }

        map $http_x_forwarded_host $best_http_host {
            default          $http_x_forwarded_host;
            ''               $this_host;
        }

        server_name_in_redirect off;
        port_in_redirect        off;

        ssl_protocols TLSv1 TLSv1.1 TLSv1.2;

        # turn on session caching to drastically improve performance
        ssl_session_cache builtin:1000 shared:SSL:10m;
        ssl_session_timeout 10m;

        # allow configuring ssl session tickets
        ssl_session_tickets on;

        # slightly reduce the time-to-first-byte
        ssl_buffer_size 4k;

        # allow configuring custom ssl ciphers
        ssl_ciphers 'ECDHE-RSA-AES128-GCM-SHA256:ECDHE-ECDSA-AES128-GCM-SHA256:ECDHE-RSA-AES256-GCM-SHA384:ECDHE-ECDSA-AES256-GCM-SHA384:DHE-RSA-AES128-GCM-SHA256:DHE-DSS-AES128-GCM-SHA256:kEDH+AESGCM:ECDHE-RSA-AES128-SHA256:ECDHE-ECDSA-AES128-SHA256:ECDHE-RSA-AES128-SHA:ECDHE-ECDSA-AES128-SHA:ECDHE-RSA-AES256-SHA384:ECDHE-ECDSA-AES256-SHA384:ECDHE-RSA-AES256-SHA:ECDHE-ECDSA-AES256-SHA:DHE-RSA-AES128-SHA256:DHE-RSA-AES128-SHA:DHE-DSS-AES128-SHA256:DHE-RSA-AES256-SHA256:DHE-DSS-AES256-SHA:DHE-RSA-AES256-SHA:AES128-GCM-SHA256:AES256-GCM-SHA384:AES128-SHA256:AES256-SHA256:AES128-SHA:AES256-SHA:AES:CAMELLIA:DES-CBC3-SHA:!aNULL:!eNULL:!EXPORT:!DES:!RC4:!MD5:!PSK:!aECDH:!EDH-DSS-DES-CBC3-SHA:!EDH-RSA-DES-CBC3-SHA:!KRB5-DES-CBC3-SHA';
        ssl_prefer_server_ciphers on;

        ssl_ecdh_curve secp384r1;

        proxy_ssl_session_reuse on;

        #nginx-lua config
        lua_shared_dict shared_data 10m;
        lua_package_path "/root/gray-engine-lua/plugin/lua-resty-http/lib/?.lua;/root/gray-engine-lua/plugin/dkjson.lua;;";
        init_worker_by_lua '
            dofile("/root/gray-engine-lua/lua/manager_cmd.lua")
            reloadInit()
        ';

    <#list http.upstreams as upstream>
        upstream ${upstream.name} {
             <#if upstream.loadbalancer_method!="">
             ${upstream.loadbalancer_method};
             </#if>
             <#list upstream.backends as backend>
             server ${backend.server}:${backend.port} max_fails=${backend.max_fails} fail_timeout=${backend.fail_timeout};
             </#list>
        }
    </#list>
    <#list http.servers as server>
        server{
            listen ${server.listen_port}<#if server.server_name=="_"> default_server</#if>;
            server_name ${server.server_name};
            <#if server.https_state=="ENABLED">
            ssl                  on;
            ssl_certificate      /etc/nginx/ssl/server.crt;
            ssl_certificate_key  /etc/nginx/ssl/server.key;
            </#if>

            <#if server.custom_code!="">
                ${server.custom_code}
            </#if>
            <#if server.server_name=="_">
            error_page  404              /404.html;
            # redirect server error pages to the static page /50x.html
            #
            error_page   500 502 503 504  /50x.html;
            location = /50x.html {
            root   /usr/local/openresty/nginx/html;
            }
            </#if>
            <#list server.locations as location>
            <#if location.path_mode=="PREFIX">
            location ^~ ${location.path} {
            <#elseif location.path_mode=="EXACT">
            location = ${location.path} {
            <#else>
            location ~* ^${location.path}\/(?<baseuri>.*) {
            </#if>

                         set $proxy_upstream_name "${location.upstream_name}";
                         set $app_id "${location.app_id}";
                         port_in_redirect off;

                         client_max_body_size                    "1m";

                         proxy_set_header Host                   $best_http_host;

                         # Pass the extracted client certificate to the backend

                         # Allow websocket connections
                         proxy_set_header                        Upgrade           $http_upgrade;
                         proxy_set_header                        Connection        $connection_upgrade;

                         proxy_set_header X-Real-IP              $the_real_ip;
                         proxy_set_header X-Forwarded-For        $the_real_ip;
                         proxy_set_header X-Forwarded-Host       $best_http_host;
                         proxy_set_header X-Forwarded-Port       $pass_port;
                         proxy_set_header X-Forwarded-Proto      $pass_access_scheme;
                         proxy_set_header X-Original-URI         $request_uri;
                         proxy_set_header X-Scheme               $pass_access_scheme;

                         # mitigate HTTPoxy Vulnerability
                         # https://www.nginx.com/blog/mitigating-the-httpoxy-vulnerability-with-nginx/
                         proxy_set_header Proxy                  "";

                         # Custom headers

                         proxy_connect_timeout                   5s;
                         proxy_send_timeout                      60s;
                         proxy_read_timeout                      60s;

                         proxy_redirect                          off;
                         proxy_buffering                         off;
                         proxy_buffer_size                       "4k";
                         proxy_buffers                           4 "4k";

                         proxy_http_version                      1.1;

                         proxy_cookie_domain                     off;
                         proxy_cookie_path                       off;

                         # In case of errors try the next upstream server before returning an error
                         proxy_next_upstream                     error timeout invalid_header http_502 http_503 http_504;
                <#if location.hasgray=="Y">
                    rewrite_by_lua_file /root/gray-engine-lua/lua/request_proxy.lua;
                </#if>
                <#if location.path_mode=="REGEX">
                                    rewrite ${location.path}/(.*) /$1 break;
                                    rewrite ${location.path} / break;
                 <#else>
                                        rewrite ${location.path}/(.*)$   ${location.access_url}/$1 break;
                 </#if>
             	proxy_pass http://$proxy_upstream_name;
             }
            </#list>
        }
     </#list >
     <#if http.default_server>
    server {
        listen 80 default_server;
        server_name  _;
        set $proxy_upstream_name "default";

        location / {
            root   /usr/local/openresty/nginx/html;
            index  index.html index.htm;
        }

        error_page  404              /404.html;

        # redirect server error pages to the static page /50x.html
        #
        error_page   500 502 503 504  /50x.html;
        location = /50x.html {
            root   /usr/local/openresty/nginx/html;
        }
    }
     </#if>

}

stream {
    log_format log_stream '[$time_local] $protocol $status $bytes_sent $bytes_received $session_time';

    access_log /var/log/nginx/access.log log_stream;

    error_log  /var/log/nginx/error.log notice;

    # TCP services
    <#list tcp.upstreams as upstream>
    upstream ${upstream.name} {
         <#if upstream.loadbalancer_method!="">
         <#if upstream.loadbalancer_method=="ip_hash">
         hash $remote_addr consistent;
         <#else>
         ${upstream.loadbalancer_method};
         </#if>
         </#if>
          <#list upstream.backends as backend>
          server ${backend.server}:${backend.port} max_fails=${backend.max_fails} fail_timeout=${backend.fail_timeout};
          </#list>
    }
     </#list>

    <#list tcp.servers as server>
    server{
           listen ${server.listen_port};

           proxy_timeout  "600s";
           proxy_pass ${server.proxy_pass};
     }
     </#list >

    # UDP services
    <#list udp.upstreams as upstream>
    upstream ${upstream.name} {
        <#if upstream.loadbalancer_method!="">
        <#if upstream.loadbalancer_method=="ip_hash">
        hash $remote_addr consistent;
        <#else>
        ${upstream.loadbalancer_method};
        </#if>
        </#if>
        <#list upstream.backends as backend>
         server ${backend.server}:${backend.port} max_fails=${backend.max_fails} fail_timeout=${backend.fail_timeout};
        </#list>
    }
     </#list>

     <#list udp.servers as server>
     server{
            listen ${server.listen_port} udp;
            proxy_responses         1;
            proxy_timeout  "600s";
            proxy_pass ${server.proxy_pass};
      }
     </#list >
}
