FROM php:8.1-apache

RUN apt-get update && apt-get install -y default-mysql-client
RUN docker-php-ext-install mysqli && docker-php-ext-enable mysqli

COPY index.php /var/www/html
COPY src /var/www/html/src
COPY .htaccess /var/www/html
COPY httpd.conf /etc/apache2/apache2.conf
COPY init.sql /docker-entrypoint-initdb.d/init.sql

RUN a2enmod rewrite

EXPOSE 8080

CMD ["apache2-foreground"]