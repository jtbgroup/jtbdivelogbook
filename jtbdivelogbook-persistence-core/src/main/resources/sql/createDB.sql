CREATE TABLE `surrogates` (
		`key_value` varchar (50), 
		`counter` numeric
        ) ENGINE=InnoDB DEFAULT CHARSET=latin1
GO
CREATE TABLE `divers` (                   
          `id` bigint(20) NOT NULL,               
          `firstname` varchar(255) default NULL,  
          `lastname` varchar(255) default NULL,   
          PRIMARY KEY  (`id`)                     
        ) ENGINE=InnoDB DEFAULT CHARSET=latin1
GO
CREATE TABLE `addresses` (                    
             `id` bigint(20) NOT NULL,                   
             `box` varchar(255) default NULL,            
             `city` varchar(255) default NULL,           
             `countryCode` varchar(255) default NULL,    
             `number` varchar(255) default NULL,         
             `postalCode` varchar(255) default NULL,     
             `region` varchar(255) default NULL,         
             `street` varchar(255) default NULL,         
             PRIMARY KEY (`id`)                         
           ) ENGINE=InnoDB DEFAULT CHARSET=latin1   
GO
CREATE TABLE `divelocations` (                   
          `id` bigint(20) NOT NULL,               
          `name` varchar(255) default NULL,  
          `depth` double default NULL,   
          `address_id` bigint(20) default NULL,  
          PRIMARY KEY (`id`),
          FOREIGN KEY (address_id) REFERENCES addresses(id)
        ) ENGINE=InnoDB DEFAULT CHARSET=latin1
GO
CREATE TABLE `divelocations_alternate_names` (                    
             `id` bigint(20) NOT NULL,                   
             `divelocation_id` bigint(20) default NULL,            
             `name` varchar(255) default NULL,         
             PRIMARY KEY (`id`),                         
    	     FOREIGN KEY (divelocation_id) REFERENCES divelocations(id)
           ) ENGINE=InnoDB DEFAULT CHARSET=latin1   
GO
CREATE TABLE `logbooks` (               
            `id` bigint(20) NOT NULL,             
            `name` varchar(255) default NULL,     
            `owner_id` bigint(20) default NULL,   
            PRIMARY KEY  (`id`),
            FOREIGN KEY (owner_id) references divers(id)
          ) ENGINE=InnoDB DEFAULT CHARSET=latin1  
GO
CREATE TABLE `dives` (                        
          `id` bigint(20) NOT NULL,                   
          `number` int(11) default NULL,              
          `depth` double default NULL,                
          `diveTime` int(11) default NULL,            
          `date` date default NULL,                   
          `comment` longblob,                         
          `waterTemperature` double default NULL,     
          `surfaceTime` int(11) default NULL,         
          `altitude` int(11) default NULL,            
          `divelocation_id` bigint(20) default NULL,  
          PRIMARY KEY  (`id`),                
          FOREIGN KEY (divelocation_id) references divelocations(id)
         ) ENGINE=InnoDB DEFAULT CHARSET=latin1 
GO
CREATE TABLE `logbooks_dives` (               
            `logbook_id` bigint(20) NOT NULL,             
            `dive_id` bigint(20) NOT NULL,             
            FOREIGN KEY (logbook_id) references logbooks(id),
            FOREIGN KEY (dive_id) references dives(id)
          ) ENGINE=InnoDB DEFAULT CHARSET=latin1  
GO
CREATE TABLE `palanquee_dive` (               
            `palanquee_id` bigint(20) NOT NULL,             
            `dive_id` bigint(20) NOT NULL,             
    	    PRIMARY KEY (palanquee_id),
            FOREIGN KEY (dive_id) references jtbdive.dives(id)
          ) ENGINE=InnoDB DEFAULT CHARSET=latin1    
GO
CREATE TABLE `palanquee` (               
            `palanquee_id` bigint(20) NOT NULL,             
            `diver_id` bigint(20) NOT NULL,
            `role` integer NOT NULL,
            FOREIGN KEY (palanquee_id) references jtbdive.palanquee_dive(palanquee_id),
            FOREIGN KEY (diver_id) references jtbdive.divers(id)
          ) ENGINE=InnoDB DEFAULT CHARSET=latin1    
GO