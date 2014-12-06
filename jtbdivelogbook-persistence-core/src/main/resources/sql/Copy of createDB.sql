CREATE DATABASE $${database_name}
GO

CREATE TABLE `divers` (                   
          `id` bigint(20) NOT NULL,               
          `firstname` varchar(255) default NULL,  
          `lastname` varchar(255) default NULL,   
          PRIMARY KEY  (`id`)                     
        ) ENGINE=InnoDB DEFAULT CHARSET=latin1
GO
        
CREATE TABLE `divelocations` (          
                 `id` bigint(20) NOT NULL,             
                 `depth` double default NULL,          
                 `name` varchar(255) default NULL,     
                 PRIMARY KEY  (`id`)                   
               ) ENGINE=InnoDB DEFAULT CHARSET=latin1  

CREATE TABLE `addresses` (                    
             `id` bigint(20) NOT NULL,                   
             `box` varchar(255) default NULL,            
             `city` varchar(255) default NULL,           
             `countryCode` varchar(255) default NULL,    
             `number` varchar(255) default NULL,         
             `postalCode` varchar(255) default NULL,     
             `region` varchar(255) default NULL,         
             `street` varchar(255) default NULL,         
             `divelocation_id` bigint(20) default NULL,  
             PRIMARY KEY  (`id`)                         
           ) ENGINE=InnoDB DEFAULT CHARSET=latin1   
                      
CREATE TABLE `logbooks` (               
            `id` bigint(20) NOT NULL,             
            `name` varchar(255) default NULL,     
            `owner_id` bigint(20) default NULL,   
            PRIMARY KEY  (`id`)                   
          ) ENGINE=InnoDB DEFAULT CHARSET=latin1   
          
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
          `logbook_id` bigint(20) default NULL,       
          PRIMARY KEY  (`id`)                         
        ) ENGINE=InnoDB DEFAULT CHARSET=latin1       
           
