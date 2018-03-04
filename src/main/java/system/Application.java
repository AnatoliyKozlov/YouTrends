/*
    This file is part of YouTrends.

    YouTrends is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    YouTrends is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with YouTrends.  If not, see <http://www.gnu.org/licenses/>.
*/

// Copyright 2017 Anatolii Kozlov
package system;

import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Log4j2
@SpringBootApplication
public class Application
{
    public static void main(String[] args)
    {
        Configurator.setLevel("system", Level.ALL);
        SpringApplication.run(Application.class, args);
    }
}