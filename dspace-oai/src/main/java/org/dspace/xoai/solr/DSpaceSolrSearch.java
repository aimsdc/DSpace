/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package org.dspace.xoai.solr;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.dspace.xoai.solr.exceptions.DSpaceSolrException;
import org.dspace.xoai.solr.exceptions.SolrSearchEmptyException;

/**
 * 
 * @author Lyncode Development Team <dspace@lyncode.com>
 */
public class DSpaceSolrSearch
{
    private static final Logger log = Logger.getLogger(DSpaceSolrSearch.class);

    public static SolrDocumentList query(SolrServer server, SolrQuery solrParams)
            throws DSpaceSolrException
    {
        try
        {
            solrParams.addSortField("item.id", ORDER.asc);
            QueryResponse response = server.query(solrParams);
            SolrDocumentList result = response.getResults();
            log.info("SolrQuery: " + solrParams.toString());
            log.info("SolrDocumentList: " + result.toString());
            return result;
        }
        catch (SolrServerException ex)
        {
            log.error("Exception executing SolrServer query - " + ex.getMessage(), ex);
            throw new DSpaceSolrException(ex.getMessage(), ex);
        }
    }

    public static SolrDocument querySingle(SolrServer server, SolrQuery solrParams)
            throws SolrSearchEmptyException
    {
        try
        {
            solrParams.addSortField("item.id", ORDER.asc);
            QueryResponse response = server.query(solrParams);
            SolrDocumentList result = response.getResults();
            if (result.getNumFound() > 0) {
                log.info("SolrQuery: " + solrParams.toString());
                log.info("SolrDocumentList: " + result.toString());
                return result.get(0);
            }
            else {
                log.info("Search returned no results");
                throw new SolrSearchEmptyException();
            }
        }
        catch (SolrServerException ex)
        {
            log.error("Exception executing SolrServer query - " + ex.getMessage(), ex);
            throw new SolrSearchEmptyException(ex.getMessage(), ex);
        }
    }
}
